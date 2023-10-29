package com.example.backend_parser.service;

import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.ThreadUtils;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class Service {
    public HttpEntity<String> parseTradingPairs() {
        return new HttpEntity<>(RequestMaker.getRequest(getTradingPairsUrl()));
    }

    public HttpEntity<?> parseOrderBooks(List<String> symbols, int time) {
        if(getThreads().isEmpty()) {
            new Thread(() -> getTokens().addAll(runParseForOrderBooks(symbols, time))).start();
        }

        if(!getTokens().isEmpty()) {
            List<Token> finalist = new ArrayList<>(getTokens());

            clearInnerData();

            return new HttpEntity<>(finalist);
        }

        return new HttpEntity<>("running");
    }

    private String getOrderBookForSymbol(String symbol) {
        return RequestMaker.getRequest(getOrderBookUrl() + symbol + getAdditionalUrlParams());
    }

    private Token parseOrderBookForSymbol(String symbol, Mapper mapper) {
        String response = getOrderBookForSymbol(symbol);

        return mapper.mapResponseToToken(response, symbol);
    }
    private List<Token> runParseForOrderBooks(List<String> symbols, int time) {
        List<Thread> threads = getThreads();
        List<Token> tokens = new ArrayList<>();

        for(String symbol : symbols) {
            Thread t = new Thread(() -> {
                Token token = parseOrderBookForSymbol(symbol, getMapper());
                tokens.add(token);
            });

            t.start();
            threads.add(t);

            ThreadUtils.sleepOnTime(time);
        }

        ThreadUtils.waitTillThreadsExecuted(threads);

        return tokens;
    }

    private void clearInnerData() {
        getThreads().clear();
        getTokens().clear();
    }

    abstract String getOrderBookUrl();
    abstract String getTradingPairsUrl();
    abstract String getAdditionalUrlParams();
    abstract List<Thread> getThreads();
    abstract List<Token> getTokens();
    abstract Mapper getMapper();
}
