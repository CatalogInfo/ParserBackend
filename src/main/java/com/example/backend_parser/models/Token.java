package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@AllArgsConstructor
@Data
public class Token {
    String symbol;
    String fullName;
    double bid;
    double ask;
    String address;
    ArrayList<Chain> chains;

    public Token(String symbol, double bid, double ask) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
    }

    public Token(String symbol, double bid, double ask, String address) {
        this.symbol = symbol;
        this.bid = bid;
        this.ask = ask;
        this.address = address;
    }
}
