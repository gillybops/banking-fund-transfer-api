package com.banking.api.repository;

import com.banking.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(
        String fromAccountNumber, String toAccountNumber);
}
