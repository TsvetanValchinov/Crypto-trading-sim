package com.example.backend.models;

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
    //TODO:it should not have setter?

    public Long getAccountId() {return accountId;}
    // TODO:it should not have setter?

    public  String getSymbol() {return this.symbol;}
    public  void setSymbol(String symbol) {this.symbol = symbol;}

    public BigDecimal getQuantity() {return quantity;}
    public void setQuantity(BigDecimal quantity) {this.quantity = quantity;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    // TODO:it should not have setter?

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}

