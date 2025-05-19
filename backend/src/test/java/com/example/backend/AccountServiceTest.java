package com.example.backend;

import com.example.backend.Crypto.CryptoAssetRepository;
import com.example.backend.Transaction.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.backend.Account.AccountService;
import com.example.backend.Account.Account;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    void testGetOrCreateUser() {
        Account acc = accountService.getOrCreateUser(42L);
        assertNotNull(acc);
        assertEquals(new BigDecimal("10000.00"), acc.getBalance());
    }

    @Test
    void testUpdateBalanceSuccess() throws InsufficientResourcesException {
        Account acc = accountService.getOrCreateUser(43L);
        accountService.updateBalance(acc.getId(), BigDecimal.valueOf(-500));
        assertEquals(new BigDecimal("9500.00"), accountService.getBalance(acc.getId()));
    }

    @Test
    void testUpdateBalanceFails() {
        Account acc = accountService.getOrCreateUser(44L);
        assertThrows(RuntimeException.class, () ->
                accountService.updateBalance(acc.getId(), BigDecimal.valueOf(-15000))
        );
    }
}
