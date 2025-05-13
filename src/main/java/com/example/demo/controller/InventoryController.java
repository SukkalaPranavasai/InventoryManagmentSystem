package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return inventoryService.getProductById(id);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return inventoryService.addProduct(product);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return inventoryService.updateProduct(id, product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        inventoryService.deleteProduct(id);
    }
}
