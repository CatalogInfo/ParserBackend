package com.example.backend_parser.models;

public class BlackListItem {
    private String symbol;
    private long time;

    public BlackListItem(String symbol, long time) {
        this.symbol = symbol;
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
