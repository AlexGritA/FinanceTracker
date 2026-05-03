package com.financetracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private TransactionType type;
    private Category category;
    private String description;
    private LocalDate date;

    public Transaction() {
    }

    public Long getId(){
        return id;
    }
    public void setId(Long newId) {
        this.id = newId;
    }

    public Double getAmount(){
        return amount;
    }
    public void setAmount(Double newAmount) {
        this.amount = newAmount;
    }

    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType newType) {
        this.type = newType;
    }

    public Category getCategory(){
        return category;
    }
    public void setCategory(Category newCategory){this.category = newCategory;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate newDate) {
        this.date = newDate;
    }

}
