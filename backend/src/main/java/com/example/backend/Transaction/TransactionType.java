package com.example.backend.Transaction;

public enum TransactionType {
    BUY,
    SELL;

    @Override
    public String toString() {
        return name();
    }

    public static TransactionType fromString(String name) {
        if(name == "BUY")
            return BUY;
        else if (name == "SELL")
            return SELL;
        else
            return null;
    }
}
