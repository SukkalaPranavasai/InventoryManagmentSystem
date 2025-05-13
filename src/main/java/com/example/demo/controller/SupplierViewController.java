package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Supplier;
import com.example.demo.service.SupplierService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/suppliers")
public class SupplierViewController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String getSuppliersPage(Model model) {
        try {
            model.addAttribute("suppliers", supplierService.findAll());
            model.addAttribute("supplier", new Supplier()); // For the form
            return "suppliers";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading suppliers: " + e.getMessage());
            return "suppliers";
        }
    }

    @PostMapping
    public String createSupplier(@Valid @ModelAttribute Supplier supplier, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid supplier data");
            return "redirect:/suppliers";
        }

        try {
            supplierService.saveSupplier(supplier);
            redirectAttributes.addFlashAttribute("success", "Supplier created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create supplier: " + e.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/update")
    public String updateSupplier(@PathVariable Long id, 
                               @Valid @ModelAttribute Supplier supplier,
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid supplier data");
            return "redirect:/suppliers";
        }

        try {
            supplierService.updateSupplier(id, supplier);
            redirectAttributes.addFlashAttribute("success", "Supplier updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update supplier: " + e.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/delete")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Supplier deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete supplier: " + e.getMessage());
        }
        return "redirect:/suppliers";
    }
}