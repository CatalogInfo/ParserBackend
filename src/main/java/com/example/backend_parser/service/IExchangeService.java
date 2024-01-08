package com.example.backend_parser.service;

import com.example.backend_parser.models.Token;
import java.util.List;

public interface IExchangeService {
    List<Token> parseTradingPairs(String authToken);
    List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken);
}
