package com.financetracker.service;

import com.financetracker.model.Category;
import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.model.User;
import com.financetracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Transaction> getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        return transactionRepository.findByUser(user);
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction updateById(Long id, Transaction updateTransaction){
        Transaction existing = transactionRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setAmount(updateTransaction.getAmount());
        existing.setCategory(updateTransaction.getCategory());
        existing.setType(updateTransaction.getType());
        existing.setDescription(updateTransaction.getDescription());
        return transactionRepository.save(existing);
    }

    public Transaction save(Transaction transaction) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        transaction.setUser(user);
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

    public List<Transaction> importCsv(MultipartFile file) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");

                String dateStr = parts[0];
                String description = parts[1];
                Double amount = Double.parseDouble(parts[2]);

                LocalDate date = LocalDate.parse(dateStr);

                Transaction transaction = new Transaction();
                TransactionType type;
                if (amount < 0) {
                    if (categoryService.isRecurring(description)) {
                        type = TransactionType.PLANNED_EXPENSE;
                    } else {
                        type = TransactionType.DAILY_EXPENSE;
                        transaction.setCategory(categoryService.categorize(description));
                    }
                } else {
                    type = TransactionType.INCOME;
                }
                amount = Math.abs(amount);

                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userService.findByUsername(username);

                transaction.setDate(date);
                transaction.setDescription(description);
                transaction.setAmount(amount);
                transaction.setType(type);
                transaction.setUser(user);

                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionRepository.saveAll(transactions);
    }

}

