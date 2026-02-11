package com.banking.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class AccountDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest {
        @NotBlank(message = "Account holder name is required")
        private String accountHolderName;
        
        @NotNull(message = "Initial balance is required")
        @Min(value = 0, message = "Initial balance must be non-negative")
        private BigDecimal initialBalance;
        
        private String currency = "USD";
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountResponse {
        private Long id;
        private String accountNumber;
        private String accountHolderName;
        private BigDecimal balance;
        private String currency;
        private String status;
        private String createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceResponse {
        private String accountNumber;
        private BigDecimal balance;
        private String currency;
    }
}
