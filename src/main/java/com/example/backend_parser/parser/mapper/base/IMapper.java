package com.example.backend_parser.parser.mapper.base;

import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.Token;

import java.util.List;

public interface IMapper {
    Token convertResponseToToken(String response, String symbol, int minAmount);
    List<Token> convertBaseQuote(String response);

    void convertChains(String response, List<Token> tokens);
}