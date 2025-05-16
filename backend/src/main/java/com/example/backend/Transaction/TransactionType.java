package com.example.backend.Transaction;

public enum TransactionType {
    BUY,
    SELL;

    @Override
    public String toString() {
        return name();
    }
}
