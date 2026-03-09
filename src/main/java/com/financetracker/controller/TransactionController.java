package com.financetracker.controller;

import com.financetracker.model.Transaction;
import com.financetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public Transaction save(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        transactionService.deleteById(id);
    }

}






















