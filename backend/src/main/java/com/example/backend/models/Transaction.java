package com.example.backend.models;

import  java.math.BigDecimal;
import  java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private Long accountId;
    private String symbol;
    private BigDecimal quantity;
    private  BigDecimal price;
    private TransactionType type;
    private LocalDateTime timestamp;
    private BigDecimal profitLoss; // calculated for sale transactions

    public Transaction() {}

    public Transaction(Long id, Long accountId, String symbol, BigDecimal quantity, BigDecimal price, TransactionType type,
                       LocalDateTime timestamp, BigDecimal profitLoss) {
        this.id = id;
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.timestamp = timestamp;
        this.profitLoss = profitLoss;
    }

    public Long getId() {return id;}

    public Long getAccountId() {return accountId;}

    public String getSymbol() {return symbol;}

    public BigDecimal getQuantity() {return quantity;}

    public BigDecimal getPrice() {return price;}

    public TransactionType getType() {return type;}

    public LocalDateTime getTimestamp() {return timestamp;}

    public BigDecimal getProfitLoss() {return profitLoss;}

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", profitLoss=" + profitLoss +
                '}';
    }
}
