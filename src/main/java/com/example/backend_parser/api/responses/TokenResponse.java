package com.example.backend_parser.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class TokenResponse {
    String symbol;
    String base;
    String quote;
    List<ChainResponse> chains;
    double bid;
    double ask;
}
