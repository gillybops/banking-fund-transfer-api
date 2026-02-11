package com.banking.api.exception;

public class InvalidTransferException extends BankingException {
    public InvalidTransferException(String message) { super(message); }
}