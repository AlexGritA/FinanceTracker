package com.financetracker.service;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public Map<String, Double> getSummary() {
        Map<String, Double> summary = new HashMap<>();
        summary.put("totalIncome", transactionRepository.sumByType(TransactionType.INCOME));
        summary.put("totalExpenses",
                transactionRepository.sumByType(TransactionType.PLANNED_EXPENSE) +
                transactionRepository.sumByType(TransactionType.DAILY_EXPENSE));
        return summary;
    }
}

