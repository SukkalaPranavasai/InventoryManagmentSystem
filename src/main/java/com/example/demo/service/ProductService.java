package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService extends BaseService<Product, Long> {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        try {
            return productRepository.findLowStockProducts();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching low stock products: " + e.getMessage());
        }
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product addProduct(Product product) {
        try {
            return save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    @Transactional
    public Product updateProduct(Long id, Product product) {
        try {
            product.setId(id);
            return update(id, product);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            super.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product: " + e.getMessage());
        }
    }
}
