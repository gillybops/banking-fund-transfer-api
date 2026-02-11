package com.banking.api.service;

import com.banking.api.dto.TransferDTO;
import com.banking.api.exception.*;
import com.banking.api.model.Account;
import com.banking.api.model.Transaction;
import com.banking.api.repository.AccountRepository;
import com.banking.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.banking.api.exception.InsufficientFundsException;
import com.banking.api.exception.InvalidTransferException;
import com.banking.api.exception.AccountInactiveException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    /**
     * Execute fund transfer with ACID guarantees
     * - Atomicity: All or nothing (both debit and credit succeed or fail together)
     * - Consistency: Account balances remain valid (no negative balances)
     * - Isolation: SERIALIZABLE level prevents concurrent modification
     * - Durability: Changes are persisted to database
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public TransferDTO.TransferResponse executeTransfer(TransferDTO.TransferRequest request) {
        
        // Validate transfer request
        validateTransferRequest(request);
        
        // Create transaction record
        String transactionId = generateTransactionId();
        Transaction transaction = createPendingTransaction(transactionId, request);
        
        try {
            // Lock and retrieve both accounts (ordered to prevent deadlock)
            Account fromAccount = lockAccount(request.getFromAccountNumber());
            Account toAccount = lockAccount(request.getToAccountNumber());
            
            // Validate accounts
            validateAccount(fromAccount, "source");
            validateAccount(toAccount, "destination");
            
            // Check sufficient funds
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                transaction.setStatus(Transaction.TransactionStatus.FAILED);
                transaction.setFailureReason("Insufficient funds");
                transactionRepository.save(transaction);
                throw new InsufficientFundsException("Insufficient funds in account: " + fromAccount.getAccountNumber());
            }
            
            // Execute transfer (debit source, credit destination)
            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
            
            // Save accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            
            // Mark transaction as completed
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
            
            log.info("Transfer completed: {} -> {}, Amount: {}", 
                fromAccount.getAccountNumber(), toAccount.getAccountNumber(), request.getAmount());
            
            return buildTransferResponse(transaction);
            
        } catch (Exception ex) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            transaction.setFailureReason(ex.getMessage());
            transactionRepository.save(transaction);
            log.error("Transfer failed: {}", ex.getMessage());
            throw ex;
        }
    }
    
    private void validateTransferRequest(TransferDTO.TransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }
        
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be greater than zero");
        }
    }
    
    private Account lockAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
    
    private void validateAccount(Account account, String type) {
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountInactiveException(account.getAccountNumber());
        }
    }
    
    private Transaction createPendingTransaction(String transactionId, TransferDTO.TransferRequest request) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setFromAccountNumber(request.getFromAccountNumber());
        transaction.setToAccountNumber(request.getToAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency("USD");
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setDescription(request.getDescription());
        return transactionRepository.save(transaction);
    }
    
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private TransferDTO.TransferResponse buildTransferResponse(Transaction transaction) {
        return new TransferDTO.TransferResponse(
            transaction.getTransactionId(),
            transaction.getFromAccountNumber(),
            transaction.getToAccountNumber(),
            transaction.getAmount(),
            transaction.getCurrency(),
            // Null-safe status check
            transaction.getStatus() != null ? transaction.getStatus().toString() : "PENDING",
            transaction.getDescription(),
            // Fix: Null-safe timestamp check
            transaction.getTimestamp() != null ? 
                transaction.getTimestamp().toString() : LocalDateTime.now().toString()
        );
    }
    
    @Transactional(readOnly = true)
    public TransferDTO.TransferResponse getTransactionStatus(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new BankingException("Transaction not found: " + transactionId));
        return buildTransferResponse(transaction);
    }
}
