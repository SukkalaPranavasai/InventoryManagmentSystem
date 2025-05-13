package com.example.demo.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrderViewController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getOrdersPage(Model model) {
        try {
            model.addAttribute("orders", orderService.findAll());
            model.addAttribute("order", new Order()); // For the form
            model.addAttribute("statuses", OrderStatus.values());
            return "orders";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading orders: " + e.getMessage());
            return "orders";
        }
    }

    @PostMapping
    public String createOrder(@Valid @ModelAttribute Order order, 
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid order data");
            return "redirect:/orders";
        }

        try {
            // Set default values only if not provided
            if (order.getOrderDate() == null) {
                order.setOrderDate(new Date());
            }
            if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
                order.setOrderNumber("ORD-" + System.currentTimeMillis());
            }
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.PENDING);
            }
            
            orderService.saveOrder(order);
            redirectAttributes.addFlashAttribute("success", "Order created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create order: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(@PathVariable Long id, 
                            @Valid @ModelAttribute Order order,
                            BindingResult result, 
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid order data");
            return "redirect:/orders";
        }

        try {
            // Preserve the ID and creation date when updating
            Order existingOrder = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setId(id);
            if (order.getOrderDate() == null) {
                order.setOrderDate(existingOrder.getOrderDate());
            }
            
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Order updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update order: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Order deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete order: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            model.addAttribute("statuses", OrderStatus.values());
            return "order-details";
        }
        return "redirect:/orders";
    }
}