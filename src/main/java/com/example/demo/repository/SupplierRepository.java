package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Supplier;

@Repository
public interface SupplierRepository extends BaseRepository<Supplier, Long> {
    @Override
    @Query(value = "ALTER TABLE suppliers AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
