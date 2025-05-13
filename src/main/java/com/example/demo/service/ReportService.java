package com.example.demo.service;

import com.example.demo.dto.ReportDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Report;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ReportRepository reportRepository;

        public List<ReportDTO> generateInventoryValuationReport() {
                List<Product> products = productRepository.findAll();
                List<ReportDTO> reportDTOs = new ArrayList<>();

                for (Product product : products) {
                        String categoryName = product.getCategory() != null ? product.getCategory().getName()
                                        : "Uncategorized";
                        Double totalValue = product.getQuantity() * product.getCostPrice();

                        ReportDTO dto = new ReportDTO(
                                        product.getId(),
                                        product.getName(),
                                        product.getSku(),
                                        categoryName,
                                        product.getQuantity(),
                                        product.getCostPrice(),
                                        product.getSellingPrice(),
                                        totalValue);

                        reportDTOs.add(dto);
                }

                return reportDTOs;
        }

        public List<ReportDTO> generateLowStockReport() {
                List<Product> lowStockProducts = productRepository.findAll().stream()
                                .filter(product -> product.getQuantity() <= product.getReorderLevel())
                                .collect(Collectors.toList());

                List<ReportDTO> reportDTOs = new ArrayList<>();

                for (Product product : lowStockProducts) {
                        String categoryName = product.getCategory() != null ? product.getCategory().getName()
                                        : "Uncategorized";
                        String supplierName = "No Supplier";
                        String contactPerson = "N/A";
                        String phone = "N/A";

                        if (product.getSupplier() != null) {
                                supplierName = product.getSupplier().getName();
                                contactPerson = product.getSupplier().getContactPerson();
                                phone = product.getSupplier().getPhone();
                        }

                        ReportDTO dto = new ReportDTO(
                                        product.getId(),
                                        product.getName(),
                                        product.getSku(),
                                        categoryName,
                                        product.getQuantity(),
                                        product.getReorderLevel(),
                                        supplierName,
                                        contactPerson,
                                        phone);

                        reportDTOs.add(dto);
                }

                return reportDTOs;
        }

        public List<Report> getAllReports() {
                return reportRepository.findAll();
        }

        public Report saveReport(Report report) {
                return reportRepository.save(report);
        }

        public void deleteReport(Long id) {
                reportRepository.deleteById(id);
        }

        public Report getReportById(Long id) {
                return reportRepository.findById(id).orElse(null);
        }
}
