package com.example.backend_parser.service;

import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class Service implements IExchangeService {
    @Override
    public List<Token> parseTradingPairs(String authToken) {
        IMapper mapper = getMapper();
        return mapper.convertBaseQuote(RequestMaker.getRequest(getTradingPairsUrl()));
    }

    @Override
    public void parseChains(String response, List<Token> tokens) {
        getMapper().convertChains(response, tokens);
    }
    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {
        getTokens().addAll(runParseForOrderBooks(tokensToSymbols(tokens), time, minAmount, authToken));
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

    private String getOrderBookForSymbol(String symbol, String authToken) {
        return RequestMaker.getRequest(getOrderBookUrl() + symbol + getAdditionalUrlParams());
    }

    private Token parseOrderBookForSymbol(String symbol, IMapper mapper, int minAmount, String authToken) {
        String response = getOrderBookForSymbol(symbol, authToken);

        return mapper.convertResponseToToken(response, symbol, minAmount);
    }

    private List<Token> runParseForOrderBooks(List<String> symbols, int time, int minAmount, String authToken) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<Future<Token>> futures = new ArrayList<>();

        for (String symbol : symbols) {
            Future<Token> future = executorService.submit(() -> parseOrderBookForSymbol(symbol, getMapper(), minAmount, authToken));
            futures.add(future);
            ThreadUtils.sleepOnTime(time);
        }

        List<Token> tokens = new ArrayList<>();
        for (Future<Token> future : futures) {
            try {
                Token token = future.get();
                tokens.add(token);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        LogFactory.makeALog("Starting termination Service");
        try {
            executorService.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LogFactory.makeALog("Ending termination Service");

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
