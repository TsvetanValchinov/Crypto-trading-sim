package com.example.backend.Crypto;

import  java.math.BigDecimal;

public class  CryptoCurrency {
    private  String symbol;
    private  String name;
    private  BigDecimal price;
    private  BigDecimal volume24h;
    private  BigDecimal changePercent24h;

    public  CryptoCurrency() {}

    public  CryptoCurrency(String symbol, String name, BigDecimal price, BigDecimal volume24h, BigDecimal changePercent24h) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.volume24h = volume24h;
        this.changePercent24h = changePercent24h;
    }

    public  String getSymbol() {return this.symbol;}
    public  void setSymbol(String symbol) {this.symbol = symbol;}

    public  String getName() {return this.name;}
    public  void setName(String name) {this.name = name;}

    public  BigDecimal getPrice() {return this.price;}
    public  void setPrice(BigDecimal price) {this.price = price;}

    public BigDecimal getChangePercent24h() {return changePercent24h;}
    public void setChangePercent24h(BigDecimal changePercent24h) {this.changePercent24h = changePercent24h;}

    public BigDecimal getVolume24h() {return volume24h;}
    public void setVolume24h(BigDecimal volume24h) {this.volume24h = volume24h;}


    @Override
    public String toString() {
        return "CryptoCurrency{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", volume24h=" + volume24h +
                ", changePercent24h=" + changePercent24h +
                '}';
    }
}
