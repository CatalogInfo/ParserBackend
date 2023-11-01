package com.example.backend_parser.service;

import com.example.backend_parser.models.BaseQuote;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface IExchangeService {
    HttpEntity<List<BaseQuote>> parseTradingPairs();
    HttpEntity<?> parseOrderBooks(List<String> symbols, int time, int minAmount);
}
