package com.example.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.ProductService;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderItemService orderItemService;

    // API to get all orders
    @GetMapping("/api/orders")
    @ResponseBody
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }
    
    // API to get a specific order
    @GetMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        try {
            Optional<Order> order = orderService.findById(id);
            if (!order.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(order.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving order: " + e.getMessage());
        }
    }

    // API to place an order
    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> payload) {
        try {
            Long productId = Long.parseLong(payload.get("productId").toString());
            Integer quantity = Integer.parseInt(payload.get("quantity").toString());
            
            // Validate input
            if (productId == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body("Invalid product ID or quantity");
            }
            
            // Check if product exists
            Optional<Product> productOpt = productService.findById(productId);
            if (!productOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Product not found");
            }
            
            Product product = productOpt.get();
            
            // Check if there's enough stock
            if (product.getQuantity() < quantity) {
                return ResponseEntity.badRequest().body("Not enough stock available");
            }
            
            // Create an order
            Order order = new Order();
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
            order.setOrderDate(new Date());
            order.setStatus(OrderStatus.PENDING);
            
            // Calculate total amount from the product price
            double totalAmount = product.getPrice() * quantity;
            order.setTotalAmount(totalAmount);
            
            // Save the order
            Order savedOrder = orderService.saveOrder(order);
            
            // Create and save order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            orderItemService.saveOrderItem(orderItem);
            
            // Update product quantity
            product.setQuantity(product.getQuantity() - quantity);
            productService.update(productId, product);
            
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error placing order: " + e.getMessage());
        }
    }
    
    // API to update an order
    @PutMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            // Check if order exists
            Optional<Order> orderOpt = orderService.findById(id);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Order order = orderOpt.get();
            
            // Get request parameters
            Long productId = Long.parseLong(payload.get("productId").toString());
            Integer quantity = Integer.parseInt(payload.get("quantity").toString());
            
            // Validate input
            if (productId == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body("Invalid product ID or quantity");
            }
            
            // Check if product exists
            Optional<Product> productOpt = productService.findById(productId);
            if (!productOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Product not found");
            }
            Product product = productOpt.get();
            
            // Get existing order items
            List<OrderItem> orderItems = orderItemService.getAllOrderItems();
            OrderItem orderItem = null;
            
            for (OrderItem item : orderItems) {
                if (item.getOrder().getId().equals(id)) {
                    orderItem = item;
                    break;
                }
            }
            
            if (orderItem == null) {
                return ResponseEntity.badRequest().body("Order item not found");
            }
            
            // Return quantity to inventory if changed
            if (!orderItem.getProduct().getId().equals(productId) || !orderItem.getQuantity().equals(quantity)) {
                // Add the old product quantity back to inventory
                Product oldProduct = orderItem.getProduct();
                oldProduct.setQuantity(oldProduct.getQuantity() + orderItem.getQuantity());
                productService.update(oldProduct.getId(), oldProduct);
                
                // Check if there's enough new product stock
                if (product.getQuantity() < quantity) {
                    return ResponseEntity.badRequest().body("Not enough stock available for the new product");
                }
                
                // Update product quantity
                product.setQuantity(product.getQuantity() - quantity);
                productService.update(productId, product);
            }
            
            // Update order item
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            orderItemService.saveOrderItem(orderItem);
            
            // Update order
            double totalAmount = product.getPrice() * quantity;
            order.setTotalAmount(totalAmount);
            Order updatedOrder = orderService.updateOrder(id, order);
            
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order: " + e.getMessage());
        }
    }

    // API to delete order
    @DeleteMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            // Check if order exists
            Optional<Order> orderOpt = orderService.findById(id);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Get the order items to restore product quantities
            List<OrderItem> items = orderItemService.findByOrderId(id);
            
            // Restore product quantities before deleting order
            for (OrderItem item : items) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productService.update(product.getId(), product);
            }
            
            // Delete the order (this will automatically delete related order items thanks to cascade)
            orderService.deleteById(id);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error deleting order: " + e.getMessage());
        }
    }
}
