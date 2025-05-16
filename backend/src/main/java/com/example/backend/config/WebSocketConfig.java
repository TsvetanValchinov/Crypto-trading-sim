package com.example.backend.config;

import com.example.backend.Crypto.KrakenWebSocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public KrakenWebSocketService krakenWebSocketService(ObjectMapper objectMapper) {
        return new KrakenWebSocketService(objectMapper);
    }
}
