package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {
    @Override
    @Query(value = "ALTER TABLE orders AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
