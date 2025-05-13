package com.example.demo.controller;

import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getProductsPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }
}