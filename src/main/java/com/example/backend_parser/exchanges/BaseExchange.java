package com.example.backend_parser.exchanges;


import com.example.backend_parser.models.Token;
import com.example.backend_parser.service.IExchangeService;

import java.io.IOException;
import java.util.List;

public abstract class BaseExchange {
    static final int MIN_AMOUNT = 2000;

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

    protected abstract IExchangeService getService();

    protected abstract int getDelayTime();

    protected abstract String getAuthToken();

    public int getMinAmount() {
        return MIN_AMOUNT;
    }

    public abstract String requestChains() throws IOException;

}
