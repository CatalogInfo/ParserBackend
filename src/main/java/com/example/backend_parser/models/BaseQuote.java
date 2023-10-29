package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuote {
    String symbol;
    String base;
    String quote;
}
