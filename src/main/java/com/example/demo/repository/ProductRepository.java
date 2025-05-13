package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    List<Product> findByQuantityLessThanEqual(Integer threshold);
    
    @Override
    @Query(value = "ALTER TABLE products AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
    
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderLevel")
    List<Product> findLowStockProducts();
}
