package com.example.backend_parser.mapper.chains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChainsKeysMapper {
    private String fullName = "full_name";
    private String isDepositEnabled = "isDepositEnabled";
    private String isWithdrawalEnabled = "isWithdrawalEnabled";
    private String fee = "fee";
}