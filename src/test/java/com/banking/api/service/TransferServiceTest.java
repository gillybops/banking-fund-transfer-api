package com.banking.api.service;

import com.banking.api.dto.TransferDTO;
import com.banking.api.exception.*;
import com.banking.api.model.Account;
import com.banking.api.model.Transaction;
import com.banking.api.repository.AccountRepository;
import com.banking.api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private TransferService transferService;
    
    private Account sourceAccount;
    private Account destinationAccount;
    
    @BeforeEach
    void setUp() {
        sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111-1111-1111");
        sourceAccount.setAccountHolderName("John Doe");
        sourceAccount.setBalance(new BigDecimal("1000.00"));
        sourceAccount.setCurrency("USD");
        sourceAccount.setStatus(Account.AccountStatus.ACTIVE);
        
        destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222-2222-2222");
        destinationAccount.setAccountHolderName("Jane Smith");
        destinationAccount.setBalance(new BigDecimal("500.00"));
        destinationAccount.setCurrency("USD");
        destinationAccount.setStatus(Account.AccountStatus.ACTIVE);
    }
    
    @Test
    void testSuccessfulTransfer() {
        // Arrange
        TransferDTO.TransferRequest request = new TransferDTO.TransferRequest(
            "1111-1111-1111",
            "2222-2222-2222",
            new BigDecimal("100.00"),
            "Test transfer"
        );
        
        when(accountRepository.findByAccountNumber("1111-1111-1111"))
            .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222-2222-2222"))
            .thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(i -> i.getArguments()[0]);
        
        // Act
        TransferDTO.TransferResponse response = transferService.executeTransfer(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
        assertEquals(new BigDecimal("900.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("600.00"), destinationAccount.getBalance());
        
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferWithInsufficientFunds() {
        // Arrange
        TransferDTO.TransferRequest request = new TransferDTO.TransferRequest(
            "1111-1111-1111",
            "2222-2222-2222",
            new BigDecimal("2000.00"),
            "Test transfer"
        );
        
        when(accountRepository.findByAccountNumber("1111-1111-1111"))
            .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222-2222-2222"))
            .thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(i -> i.getArguments()[0]);
        
        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> {
            transferService.executeTransfer(request);
        });
        
        verify(accountRepository, never()).save(any(Account.class));
    }
    
    @Test
    void testTransferToSameAccount() {
        // Arrange
        TransferDTO.TransferRequest request = new TransferDTO.TransferRequest(
            "1111-1111-1111",
            "1111-1111-1111",
            new BigDecimal("100.00"),
            "Test transfer"
        );
        
        // Act & Assert
        assertThrows(InvalidTransferException.class, () -> {
            transferService.executeTransfer(request);
        });
    }
    
    @Test
    void testTransferFromInactiveAccount() {
        // Arrange
        sourceAccount.setStatus(Account.AccountStatus.FROZEN);
        
        TransferDTO.TransferRequest request = new TransferDTO.TransferRequest(
            "1111-1111-1111",
            "2222-2222-2222",
            new BigDecimal("100.00"),
            "Test transfer"
        );
        
        when(accountRepository.findByAccountNumber("1111-1111-1111"))
            .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222-2222-2222"))
            .thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(i -> i.getArguments()[0]);
        
        // Act & Assert
        assertThrows(AccountInactiveException.class, () -> {
            transferService.executeTransfer(request);
        });
    }
    
    @Test
    void testTransferToNonExistentAccount() {
        // Arrange
        TransferDTO.TransferRequest request = new TransferDTO.TransferRequest(
            "1111-1111-1111",
            "9999-9999-9999",
            new BigDecimal("100.00"),
            "Test transfer"
        );
        
        when(accountRepository.findByAccountNumber("1111-1111-1111"))
            .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("9999-9999-9999"))
            .thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(i -> i.getArguments()[0]);
        
        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            transferService.executeTransfer(request);
        });
    }
}
