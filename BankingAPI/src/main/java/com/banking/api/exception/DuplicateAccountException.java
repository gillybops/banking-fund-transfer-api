package com.banking.api.exception;

public class DuplicateAccountException extends BankingException {
    public DuplicateAccountException(String accountNumber) {
        super("Account already exists: " + accountNumber);
    }
}