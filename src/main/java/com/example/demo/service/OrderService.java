package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService extends BaseService<Order, Long> {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order saveOrder(Order order) {
        try {
            if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
                order.setOrderNumber("ORD-" + System.currentTimeMillis());
            }
            if (order.getOrderDate() == null) {
                order.setOrderDate(new Date());
            }
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.PENDING);
            }
            return save(order);
        } catch (Exception e) {
            throw new RuntimeException("Error saving order: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getActiveOrders() {
        try {
            return orderRepository.findAll().stream()
                    .filter(order -> OrderStatus.PENDING.equals(order.getStatus()) ||
                            OrderStatus.PROCESSING.equals(order.getStatus()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching active orders: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getRecentOrders(int limit) {
        try {
            return orderRepository.findAll().stream()
                    .filter(order -> order.getOrderDate() != null)
                    .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching recent orders: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            super.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order: " + e.getMessage());
        }
    }

    @Transactional
    public Order updateOrder(Long id, Order order) {
        try {
            order.setId(id);
            return update(id, order);
        } catch (Exception e) {
            throw new RuntimeException("Error updating order: " + e.getMessage());
        }
    }
}
