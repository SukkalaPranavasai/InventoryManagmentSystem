package com.example.demo.controller;

import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportViewController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String getReportsPage(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "reports";
    }
}