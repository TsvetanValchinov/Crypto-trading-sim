package com.example.backend.Crypto;

import  java.math.BigDecimal;
import  java.time.LocalDateTime;

public class CryptoAsset {
    private  Long id;
    private  Long accountId;
    private String symbol;
    private  BigDecimal quantity;
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    public  CryptoAsset() {}

    public  CryptoAsset(Long id, Long accountId, String symbol, BigDecimal quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getAccountId() {return accountId;}
    public void setAccountId(Long accountId) {this.accountId = accountId;}

    public  String getSymbol() {return this.symbol;}
    public  void setSymbol(String symbol) {this.symbol = symbol;}

    public BigDecimal getQuantity() {return quantity;}
    public void setQuantity(BigDecimal quantity) {this.quantity = quantity;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    @Override
    public String toString() {
        return "CryptoAsset{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

