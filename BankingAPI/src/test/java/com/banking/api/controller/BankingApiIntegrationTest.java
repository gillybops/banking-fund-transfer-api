package com.banking.api.controller;

import com.banking.api.dto.AccountDTO;
import com.banking.api.dto.TransferDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
class BankingApiIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testCompleteTransferWorkflow() throws Exception {
        // 1. Create source account
        AccountDTO.CreateAccountRequest sourceRequest = new AccountDTO.CreateAccountRequest(
            "John Doe", new BigDecimal("1000.00"), "USD"
        );
        
        MvcResult sourceResult = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sourceRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accountHolderName").value("John Doe"))
            .andExpect(jsonPath("$.balance").value(1000.00))
            .andReturn();
        
        AccountDTO.AccountResponse sourceAccount = objectMapper.readValue(
            sourceResult.getResponse().getContentAsString(),
            AccountDTO.AccountResponse.class
        );
        
        // 2. Create destination account
        AccountDTO.CreateAccountRequest destRequest = new AccountDTO.CreateAccountRequest(
            "Jane Smith", new BigDecimal("500.00"), "USD"
        );
        
        MvcResult destResult = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accountHolderName").value("Jane Smith"))
            .andExpect(jsonPath("$.balance").value(500.00))
            .andReturn();
        
        AccountDTO.AccountResponse destAccount = objectMapper.readValue(
            destResult.getResponse().getContentAsString(),
            AccountDTO.AccountResponse.class
        );
        
        // 3. Execute transfer
        TransferDTO.TransferRequest transferRequest = new TransferDTO.TransferRequest(
            sourceAccount.getAccountNumber(),
            destAccount.getAccountNumber(),
            new BigDecimal("250.00"),
            "Integration test transfer"
        );
        
        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.amount").value(250.00));
        
        // 4. Verify source account balance
        mockMvc.perform(get("/api/v1/accounts/" + sourceAccount.getAccountNumber() + "/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(750.00));
        
        // 5. Verify destination account balance
        mockMvc.perform(get("/api/v1/accounts/" + destAccount.getAccountNumber() + "/balance"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(750.00));
    }
    
    @Test
    void testTransferWithInsufficientFunds() throws Exception {
        // Create accounts
        AccountDTO.CreateAccountRequest sourceRequest = new AccountDTO.CreateAccountRequest(
            "Poor Person", new BigDecimal("50.00"), "USD"
        );
        
        MvcResult sourceResult = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sourceRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        AccountDTO.AccountResponse sourceAccount = objectMapper.readValue(
            sourceResult.getResponse().getContentAsString(),
            AccountDTO.AccountResponse.class
        );
        
        AccountDTO.CreateAccountRequest destRequest = new AccountDTO.CreateAccountRequest(
            "Rich Person", new BigDecimal("1000.00"), "USD"
        );
        
        MvcResult destResult = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        
        AccountDTO.AccountResponse destAccount = objectMapper.readValue(
            destResult.getResponse().getContentAsString(),
            AccountDTO.AccountResponse.class
        );
        
        // Attempt transfer with insufficient funds
        TransferDTO.TransferRequest transferRequest = new TransferDTO.TransferRequest(
            sourceAccount.getAccountNumber(),
            destAccount.getAccountNumber(),
            new BigDecimal("1000.00"),
            "Should fail"
        );
        
        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Insufficient funds in account: " 
                + sourceAccount.getAccountNumber()));
    }
}
