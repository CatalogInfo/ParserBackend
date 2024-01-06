package com.example.backend_parser.service;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Service implements IExchangeService {
    @Override
    public List<Token> parseTradingPairs() {
        IMapper mapper = getMapper();
        return mapper.convertBaseQuote(RequestMaker.getRequest(getTradingPairsUrl()));
    }
    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount) {
        getTokens().addAll(runParseForOrderBooks(tokensToSymbols(tokens), time, minAmount));
        ArrayList<Token> finalList = new ArrayList<>(getTokens());
        clearInnerData();
        return finalList;
    }

    private List<String> tokensToSymbols(List<Token> token) {
        List<String> symbolList = token.stream()
                .map(Token::getSymbol)
                .collect(Collectors.toList());
        return symbolList;
    }

    private String getOrderBookForSymbol(String symbol) {
        return RequestMaker.getRequest(getOrderBookUrl() + symbol + getAdditionalUrlParams());
    }

    private Token parseOrderBookForSymbol(String symbol, IMapper mapper, int minAmount) {
        String response = getOrderBookForSymbol(symbol);

        return mapper.convertResponseToToken(response, symbol, minAmount);
    }

    private List<Token> runParseForOrderBooks(List<String> symbols, int time, int minAmount) {
        List<Thread> threads = getThreads();
        List<Token> tokens = new ArrayList<>();

        for(String symbol : symbols) {
            Thread t = new Thread(() -> {
                Token token = parseOrderBookForSymbol(symbol, getMapper(), minAmount);
                synchronized (tokens) {
                    tokens.add(token);
                }

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
    abstract IMapper getMapper();
}
