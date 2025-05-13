package com.example.demo.service;

import com.example.demo.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {
    protected final BaseRepository<T, ID> repository;

    protected BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Transactional
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving entity: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
            if (repository.count() == 0) {
                repository.resetAutoIncrement();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entity: " + e.getMessage());
        }
    }

    @Transactional
    public T update(ID id, T entity) {
        try {
            if (repository.existsById(id)) {
                return repository.save(entity);
            }
            throw new RuntimeException("Entity not found with id: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Error updating entity: " + e.getMessage());
        }
    }
} 