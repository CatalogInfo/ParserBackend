package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Data
public class Token {
    String symbol;
    String fullName;
    int decimals;
    double bid;
    double ask;
    String base;
    String quote;
    String address;
    ArrayList<Chain> chains = new ArrayList<>();

    public Token(String symbol, double bid, double ask) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
    }

    public Token(String symbol, String base, String quote) {
        this.symbol = symbol;
        this.base = base;
        this.quote = quote;
    }

    public Token(String symbol, double bid, double ask, String address) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
        this.address = address;
    }

    public Token(String symbol, int decimals, String address, String base, String quote) {
        this.symbol = symbol;
        this.decimals = decimals;
        this.address = address;
        this.base = base;
        this.quote = quote;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public void addChain(Chain chain) {
        chains.add(chain);
    }
}
