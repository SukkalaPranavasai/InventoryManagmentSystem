package com.example.demo.controller;

import com.example.demo.dto.ReportDTO;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/inventory-valuation")
    public List<ReportDTO> getInventoryValuationReport() {
        return reportService.generateInventoryValuationReport();
    }

    @GetMapping("/low-stock")
    public List<ReportDTO> getLowStockReport() {
        return reportService.generateLowStockReport();
    }
}
