package com.example.backend.models;

import  java.math.BigDecimal;
import  java.time.LocalDateTime;

public class Account {
    private  Long id;
    private  Long userId;
    private  BigDecimal balance;
    private  BigDecimal initialBalance;
    private  LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

    public  Account() {}

    public  Account(Long id, Long userId, BigDecimal balance, BigDecimal initialBalance, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.initialBalance = initialBalance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public  Long getId() {return this.id;}
    public  void setId(Long id) {this.id = id;} // TODO: should it have setter?

    public  Long getUserId() {return this.userId;}
    public  void setUserId(Long userId) {this.userId = userId;}// TODO: should it have setter?

    public  BigDecimal getBalance() {return this.balance;}
    public  void  setBalance(BigDecimal balance) {this.balance = balance;}

    public BigDecimal getInitialBalance() {return initialBalance;}
    public void setInitialBalance(BigDecimal initialBalance) {this.initialBalance = initialBalance;}

    public  LocalDateTime getCreatedAt() {return this.createdAt;}
    // no setter for the createdAt field

    public  LocalDateTime getUpdatedAt() {return this.updatedAt;}
    public  void  setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                ", initialBalance=" + initialBalance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}

