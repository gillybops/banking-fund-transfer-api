package com.banking.api.exception;

public class AccountInactiveException extends BankingException {
    public AccountInactiveException(String message) { super(message); }
}