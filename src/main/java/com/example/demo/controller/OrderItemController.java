package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.OrderItem;
import com.example.demo.service.OrderItemService;

@RestController
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/api/order-items")
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }
    
    @GetMapping("/api/orderitems")
    public List<OrderItem> getOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @PostMapping("/api/order-items")
    public OrderItem addOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.saveOrderItem(orderItem);
    }
}
