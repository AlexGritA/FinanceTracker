package com.financetracker.service;

import com.financetracker.model.Transaction;
import com.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
