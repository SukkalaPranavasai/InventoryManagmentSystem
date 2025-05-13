package com.example.demo.service;

import com.example.demo.model.Supplier;
import com.example.demo.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService extends BaseService<Supplier, Long> {
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        super(supplierRepository);
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        try {
            return save(supplier);
        } catch (Exception e) {
            throw new RuntimeException("Error saving supplier: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            // Check if supplier has associated products before deletion
            Optional<Supplier> supplier = findById(id);
            if (supplier.isPresent() && !supplier.get().getProducts().isEmpty()) {
                throw new RuntimeException("Cannot delete supplier with associated products");
            }
            super.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting supplier: " + e.getMessage());
        }
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier supplier) {
        try {
            supplier.setId(id);
            return update(id, supplier);
        } catch (Exception e) {
            throw new RuntimeException("Error updating supplier: " + e.getMessage());
        }
    }
}
