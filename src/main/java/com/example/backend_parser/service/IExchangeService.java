package com.example.backend_parser.service;

import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.Token;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface IExchangeService {
    List<Token> parseTradingPairs();
    List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount);
}
