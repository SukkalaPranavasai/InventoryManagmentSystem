package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE ${T} AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
} 