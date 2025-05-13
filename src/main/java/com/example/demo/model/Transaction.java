package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Enumerated(EnumType.STRING)  // ✅ Fixes the issue
    private TransactionType type; // ✅ Enum instead of String

    private Double amount;

    // ✅ Getters
    public Long getId() { return id; }
    public Date getTransactionDate() { return transactionDate; }
    public TransactionType getType() { return type; }
    public Double getAmount() { return amount; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
    public void setType(TransactionType type) { this.type = type; } // ✅ Fixing the warning
    public void setAmount(Double amount) { this.amount = amount; }
}
