package com.banking.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class TransferDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferRequest {
        @NotBlank(message = "Source account number is required")
        private String fromAccountNumber;
        
        @NotBlank(message = "Destination account number is required")
        private String toAccountNumber;
        
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Transfer amount must be greater than 0")
        private BigDecimal amount;
        
        private String description;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferResponse {
        private String transactionId;
        private String fromAccountNumber;
        private String toAccountNumber;
        private BigDecimal amount;
        private String currency;
        private String status;
        private String description;
        private String timestamp;
    }
}
