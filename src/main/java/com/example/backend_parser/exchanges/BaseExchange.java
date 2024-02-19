package com.example.backend_parser.exchanges;


import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.splitter.Splitter;

import java.io.IOException;
import java.util.List;

public abstract class BaseExchange {
    public static int MIN_AMOUNT = Splitter.options.getMinAmount();

    public List<Token> getTradingPairs() {
        return getService().parseTradingPairs(getAuthToken());
    }

    public void getChains(String response, List<Token> tokens) {
        getService().parseChains(response, tokens);
    }


    public List<Token> getOrderBooks(List<Token> tokens, Integer minAmount) {
        if (minAmount == null)
            minAmount = MIN_AMOUNT;

        return getService().parseOrderBooks(tokens, getDelayTime(), minAmount, getAuthToken());
    }

    public Token getOrderBookForToken(Token token) {
        return getService().parseOrderBookForToken(token, MIN_AMOUNT);
    }

    protected abstract IExchangeService getService();

    protected abstract int getDelayTime();

    protected abstract String getAuthToken();

    public int getMinAmount() {
        return MIN_AMOUNT;
    }

    public abstract String requestChains() throws IOException;



}
