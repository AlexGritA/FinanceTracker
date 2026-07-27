package com.financetracker.repository;

import com.financetracker.model.Transaction;
import com.financetracker.model.TransactionType;
import com.financetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository <Transaction, Long>{

    @Query("SELECT COALESCE (SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    Double sumByType(@Param("type") TransactionType type);

    List<Transaction> findByUser(User user);
}
