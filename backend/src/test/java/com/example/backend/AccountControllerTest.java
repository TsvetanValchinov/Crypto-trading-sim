package com.example.backend;

import com.example.backend.Transaction.TradeRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetBalance() throws Exception {
        // First create or retrieve an account
        mockMvc.perform(post("/api/account/user/110"))
                .andExpect(status().isOk());

        // Get account ID = 1 for this simple test (assuming fresh DB)
        mockMvc.perform(get("/api/account/1/balance"))
                .andExpect(status().isOk());
    }

    @Test
    void testBuyAndSellFlow() throws Exception {
        TradeRequestDTO buy = new TradeRequestDTO();
        buy.setSymbol("BTC/USD");
        buy.setQuantity(0.1);

        mockMvc.perform(post("/api/account/1/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buy)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/account/1/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buy)))
                .andExpect(status().isOk());
    }

    @Test
    void testResetAccount() throws Exception {
        mockMvc.perform(post("/api/account/1/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account reset"));
    }
}

