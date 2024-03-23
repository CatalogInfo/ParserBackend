package com.example.backend_parser.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ChainResponse {
    String name;
    boolean isDepositEnabled;
    boolean isWithdrawalEnabled;
    double fee;
    double feePercent;
}