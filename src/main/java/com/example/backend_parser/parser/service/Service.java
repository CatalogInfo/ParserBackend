package com.example.backend_parser.parser.service;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.parser.mapper.base.IMapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.ThreadUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class Service implements IExchangeService {
    @Override
    public List<Token> parseTradingPairs(String authToken) {
        IMapper mapper = getMapper();
        try {
            return mapper.convertBaseQuote(HttpClientMaker.get(getTradingPairsUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
        try {
            return HttpClientMaker.get(getOrderBookUrl() + symbol + getAdditionalUrlParams());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Token parseOrderBookForToken(Token token, int minAmount) {
        return parseOrderBookForSymbol(token.getSymbol(), getMapper(), minAmount, "");
    }

    private Token parseOrderBookForSymbol(String symbol, IMapper mapper, int minAmount, String authToken) {
        String response = getOrderBookForSymbol(symbol, authToken);

        return mapper.convertResponseToToken(response, symbol, minAmount);
    }

    private List<Token> runParseForOrderBooks(List<String> symbols, int time, int minAmount, String authToken) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
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
        LogFactory.makeALog("Starting termination");
        try {
            executorService.awaitTermination(100, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LogFactory.makeALog("Ending termination");

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
