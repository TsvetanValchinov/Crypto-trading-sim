package com.example.backend.models;

import  java.math.BigDecimal;

public class CryptoCurrency {
    private  String symbol;
    private  String name;
    private  BigDecimal price;

    public  CryptoCurrency() {}

    public  CryptoCurrency(String symbol, String name, BigDecimal price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public  String getSymbol() {return this.symbol;}
    public  void setSymbol(String symbol) {this.symbol = symbol;}

    public  String getName() {return this.name;}
    public  void setName(String name) {this.name = name;}

    public  BigDecimal getPrice() {return this.price;}
    public  void setPrice(BigDecimal price) {this.price = price;}
}
