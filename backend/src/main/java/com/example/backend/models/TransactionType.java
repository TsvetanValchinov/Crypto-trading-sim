package com.example.backend.models;

public enum TransactionType {
    BUY,
    SELL;

    @Override
    public String toString() {
        return name();
    }
}
