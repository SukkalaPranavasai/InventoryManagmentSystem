package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderItemRepository;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
    
    @Transactional
    public void deleteOrderItemsByOrder(Order order) {
        orderItemRepository.deleteByOrderId(order.getId());
    }
    
    @Transactional
    public void deleteOrderItemsByProduct(Product product) {
        orderItemRepository.deleteByProductId(product.getId());
    }
    
    @Transactional
    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<OrderItem> findByProductId(Long productId) {
        return orderItemRepository.findByProductId(productId);
    }
}
