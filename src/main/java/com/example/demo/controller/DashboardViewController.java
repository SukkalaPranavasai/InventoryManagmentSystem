package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.SupplierService;

@Controller
@RequestMapping("/")
public class DashboardViewController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String getDashboardPage(Model model) {
        try {
            // Add actual metrics using the services
            model.addAttribute("totalProducts", productService.findAll().size());
            model.addAttribute("activeOrders", orderService.getActiveOrders().size());
            model.addAttribute("totalSuppliers", supplierService.findAll().size());
            model.addAttribute("lowStockItems", productService.getLowStockProducts().size());
            model.addAttribute("recentOrders", orderService.getRecentOrders(5)); // Get last 5 orders
            model.addAttribute("lowStockAlerts", productService.getLowStockProducts());
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "index";
        }
    }
}