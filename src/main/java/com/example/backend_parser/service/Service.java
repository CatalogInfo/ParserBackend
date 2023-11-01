package com.example.backend_parser.service;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.ThreadUtils;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class Service implements IExchangeService{
    @Override
    public HttpEntity<List<BaseQuote>> parseTradingPairs() {
        IMapper mapper = getMapper();
        return new HttpEntity<>(mapper.convertBaseQuote(RequestMaker.getRequest(getTradingPairsUrl())));
    }
    @Override
    public HttpEntity<?> parseOrderBooks(List<String> symbols, int time, int minAmount) {
        if(getThreads().isEmpty()) {
            new Thread(() -> getTokens().addAll(runParseForOrderBooks(symbols, time, minAmount))).start();
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
