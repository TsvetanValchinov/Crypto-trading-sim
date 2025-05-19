package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.backend.Crypto.CryptoAssetService;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CryptoAssetServiceTest {

    @Autowired
    private CryptoAssetService cryptoAssetService;

    @Test
    void testAddAndGetAsset() {
        cryptoAssetService.addOrUpdateAsset(1L, "ETH/USD", BigDecimal.valueOf(1.0));
        BigDecimal quantity = cryptoAssetService.getQuantity(1L, "ETH/USD");
        assertEquals(BigDecimal.valueOf(1.0), quantity);
    }

    @Test
    void testUpdateAssetQuantity() {
        cryptoAssetService.updateQuantity(1L, "ETH/USD", BigDecimal.valueOf(2.5));
        BigDecimal updated = cryptoAssetService.getQuantity(1L, "ETH/USD");
        assertEquals(BigDecimal.valueOf(2.5), updated);
    }
}

