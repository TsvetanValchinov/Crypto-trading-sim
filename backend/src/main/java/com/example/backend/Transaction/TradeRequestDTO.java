package com.example.backend.Transaction;

public class TradeRequestDTO {
    private String symbol;
    private double quantity;

    public String getSymbol() {return symbol;}
    public void setSymbol(String symbol) {this.symbol = symbol;}

    public double getQuantity() {return quantity;}
    public void setQuantity(double quantity) {this.quantity = quantity;}
}
