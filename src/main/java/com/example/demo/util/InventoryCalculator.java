package com.example.demo.util;

import com.example.demo.model.Product;

public class InventoryCalculator {

    public static double calculateInventoryValue(Product product) {
        return product.getQuantity() * product.getPrice();
    }
}
