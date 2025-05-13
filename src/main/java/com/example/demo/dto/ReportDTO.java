package com.example.demo.dto;

public class ReportDTO {
    private Long id;
    private String name;
    private String sku;
    private String categoryName;
    private Integer quantity;
    private Double costPrice;
    private Double sellingPrice;
    private Double totalValue;
    private Integer reorderLevel;
    private String supplierName;
    private String contactPerson;
    private String phone;

    // Default Constructor
    public ReportDTO() {
    }

    // Constructor for Inventory Valuation Report
    public ReportDTO(Long id, String name, String sku, String categoryName, Integer quantity,
            Double costPrice, Double sellingPrice, Double totalValue) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.totalValue = totalValue;
    }

    // Constructor for Low Stock Report
    public ReportDTO(Long id, String name, String sku, String categoryName, Integer quantity,
            Integer reorderLevel, String supplierName, String contactPerson, String phone) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.phone = phone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setSupplierContact(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setSupplierPhone(String phone) {
        this.phone = phone;
    }
}
