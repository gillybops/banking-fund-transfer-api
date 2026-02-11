package com.banking.api.repository;

import com.banking.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    
    boolean existsByAccountNumber(String accountNumber);
    
    // Pessimistic locking to prevent race conditions during transfers
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountNumber(String accountNumber);
}
