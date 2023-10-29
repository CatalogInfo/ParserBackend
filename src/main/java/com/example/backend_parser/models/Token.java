package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Token {
    String symbol;
    double bid;
    double ask;
}
