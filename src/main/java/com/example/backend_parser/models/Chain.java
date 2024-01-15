package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Chain {
    String name;
    boolean isDepositEnabled;
    boolean isWithdrawalEnabled;
    double fee;
    double feePercent = 0;

    public Chain(String name, boolean isDepositEnabled, boolean isWithdrawalEnabled, double fee) {
        this.name = name;
        this.isDepositEnabled = isDepositEnabled;
        this.isWithdrawalEnabled = isWithdrawalEnabled;
        this.fee = fee;
    }

    public Chain(String name, boolean isDepositEnabled, boolean isWithdrawalEnabled) {
        this.name = name;
        this.isDepositEnabled = isDepositEnabled;
        this.isWithdrawalEnabled = isWithdrawalEnabled;
    }

    @Override
    public String toString() {
        return "Chain{" +
                "name='" + name + '\'' +
                ", isDepositEnabled=" + isDepositEnabled +
                ", isWithdrawalEnabled=" + isWithdrawalEnabled +
                ", fee=" + fee +
                ", feePercent=" + feePercent +
                '}';
    }
}
