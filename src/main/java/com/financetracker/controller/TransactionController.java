package com.financetracker.controller;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Long id) {
        return transactionService.getById(id);
    }
    @GetMapping("/summary")
    public Map<String, Double> getSummary() {
        return transactionService.getSummary();
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id,@RequestBody Transaction transaction) {
        return transactionService.updateById(id, transaction);
    }

    @PostMapping
    public Transaction save(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        transactionService.deleteById(id);
    }

}






















