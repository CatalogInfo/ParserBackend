package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.parser.mapper.exchanges.BinanceMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;
import java.io.IOException;

public class PoloniexExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.poloniex.com/markets/BTC_USDT/orderBook",
            "https://api.poloniex.com/markets",
            new BinanceMapper(),
            "?limit=100"
    );
    @Override
    protected IExchangeService getService() {
        return service;
    }

    @Override
    protected int getDelayTime() {
        return 100;
    }

    @Override
    protected String getAuthToken() {
        return null;
    }

    public String requestChains() throws IOException {

        return null;
    }
}