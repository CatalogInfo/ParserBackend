package com.example.backend_parser.mapper;

import com.example.backend_parser.models.Token;

public interface Mapper {
    Token mapResponseToToken(String response, String symbol);
}
