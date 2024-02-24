package com.example.backend_parser.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ExchangeResponse {
    String link;
    String name;
    List<TokenResponse> tokens;
    List<String> bannedTokens;
}
