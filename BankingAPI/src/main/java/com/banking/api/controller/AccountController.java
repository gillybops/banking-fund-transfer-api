package com.banking.api.controller;

import com.banking.api.dto.AccountDTO;
import com.banking.api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountService accountService;
    
    /**
     * Create a new bank account
     */
    @PostMapping
    public ResponseEntity<AccountDTO.AccountResponse> createAccount(
            @Valid @RequestBody AccountDTO.CreateAccountRequest request) {
        AccountDTO.AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get account details by account number
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO.AccountResponse> getAccount(
            @PathVariable String accountNumber) {
        AccountDTO.AccountResponse response = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get account balance
     */
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountDTO.BalanceResponse> getBalance(
            @PathVariable String accountNumber) {
        AccountDTO.BalanceResponse response = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(response);
    }
    
    /**
     * List all accounts
     */
    @GetMapping
    public ResponseEntity<List<AccountDTO.AccountResponse>> getAllAccounts() {
        List<AccountDTO.AccountResponse> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
