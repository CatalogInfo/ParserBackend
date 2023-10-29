package com.example.backend_parser.service;

import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.models.Token;

import java.util.ArrayList;
import java.util.List;

public class ServiceEntity extends Service {
    private final List<Thread> threads = new ArrayList<>();
    private final List<Token> tokens = new ArrayList<>();
    private final String tradingPairsUrl;
    private final String orderBookUrl;
    private final Mapper mapper;
    private String additionalUrlParams = "";

    public ServiceEntity(String orderBookUrl, String tradingPairsUrl, Mapper mapper) {
        this.orderBookUrl = orderBookUrl;
        this.tradingPairsUrl = tradingPairsUrl;
        this.mapper = mapper;
    }

    public ServiceEntity(String orderBookUrl, String tradingPairsUrl, Mapper mapper, String additionalUrlParams) {
        this.orderBookUrl = orderBookUrl;
        this.tradingPairsUrl = tradingPairsUrl;
        this.mapper = mapper;
        this.additionalUrlParams = additionalUrlParams;
    }

    @Override
    String getOrderBookUrl() {
        return this.orderBookUrl;
    }
    @Override
    String getTradingPairsUrl() {
        return this.tradingPairsUrl;
    }
    @Override
    String getAdditionalUrlParams() {
        return this.additionalUrlParams;
    }
    @Override
    Mapper getMapper() {
        return this.mapper;
    }
    @Override
    List<Thread> getThreads() {
        return this.threads;
    }
    @Override
    List<Token> getTokens() {
        return tokens;
    }
}
