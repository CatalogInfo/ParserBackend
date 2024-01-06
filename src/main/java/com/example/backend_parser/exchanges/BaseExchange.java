package com.example.backend_parser.exchanges;


import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.service.IExchangeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseExchange {
    static final int MIN_AMOUNT = 2000;

    public List<Token> getTradingPairs() {
        return getService().parseTradingPairs();
    }

    public List<Token> getOrderBooks(List<Token> tokens, Integer minAmount) {
        if (minAmount == null)
            minAmount = MIN_AMOUNT;

        return getService().parseOrderBooks(tokens, getDelayTime(), minAmount);
    }

    protected abstract IExchangeService getService();

    protected abstract int getDelayTime();

    public int getMinAmount() {
        return MIN_AMOUNT;
    }

}
