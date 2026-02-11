package com.banking.api.service;

import com.banking.api.dto.AccountDTO;
import com.banking.api.exception.AccountNotFoundException;
import com.banking.api.exception.DuplicateAccountException;
import com.banking.api.model.Account;
import com.banking.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final Random random = new Random();
    
    @Transactional
    public AccountDTO.AccountResponse createAccount(AccountDTO.CreateAccountRequest request) {
        // Generate unique account number
        String accountNumber = generateAccountNumber();
        
        // Ensure uniqueness
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }
        
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getInitialBalance());
        account.setCurrency(request.getCurrency());
        account.setStatus(Account.AccountStatus.ACTIVE);
        
        Account savedAccount = accountRepository.save(account);
        log.info("Created account: {} for {}", accountNumber, request.getAccountHolderName());
        
        return mapToResponse(savedAccount);
    }
    
    @Transactional(readOnly = true)
    public AccountDTO.AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return mapToResponse(account);
    }
    
    @Transactional(readOnly = true)
    public AccountDTO.BalanceResponse getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        return new AccountDTO.BalanceResponse(
            account.getAccountNumber(),
            account.getBalance(),
            account.getCurrency()
        );
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO.AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    private String generateAccountNumber() {
        // Generate account number in format: XXXX-XXXX-XXXX
        return String.format("%04d-%04d-%04d",
            random.nextInt(10000),
            random.nextInt(10000),
            random.nextInt(10000));
    }
    
    private AccountDTO.AccountResponse mapToResponse(Account account) {
        return new AccountDTO.AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getAccountHolderName(),
            account.getBalance(),
            account.getCurrency(),
            account.getStatus().toString(),
            account.getCreatedAt().toString()
        );
    }
}
