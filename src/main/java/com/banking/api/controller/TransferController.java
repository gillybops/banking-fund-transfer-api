package com.banking.api.controller;

import com.banking.api.dto.TransferDTO;
import com.banking.api.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {
    
    private final TransferService transferService;
    
    /**
     * Execute a fund transfer between accounts
     */
    @PostMapping
    public ResponseEntity<TransferDTO.TransferResponse> executeTransfer(
            @Valid @RequestBody TransferDTO.TransferRequest request) {
        TransferDTO.TransferResponse response = transferService.executeTransfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Get transaction status by transaction ID
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransferDTO.TransferResponse> getTransactionStatus(
            @PathVariable String transactionId) {
        TransferDTO.TransferResponse response = transferService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(response);
    }
}
