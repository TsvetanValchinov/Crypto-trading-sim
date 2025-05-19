package com.example.backend;

import com.example.backend.Transaction.Transaction;
import com.example.backend.Transaction.TransactionType;
import com.example.backend.Transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void testBuyCrypto() throws InsufficientResourcesException {
        Transaction tx = transactionService.buyCrypto(1L, "BTC/USD", BigDecimal.valueOf(0.01));
        assertEquals("BTC/USD", tx.getSymbol());
        assertEquals(TransactionType.BUY, tx.getType());
    }

    @Test
    void testSellCryptoInsufficientFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.sellCrypto(1L, "BTC/USD", BigDecimal.valueOf(1000));
        });
    }
}
