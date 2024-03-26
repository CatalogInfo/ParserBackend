package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.KucoinMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;

import java.io.IOException;

public class KucoinExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.kucoin.com/api/v1/market/orderbook/level2_100?symbol=",
            "https://api.kucoin.com/api/v2/symbols",
            new KucoinMapper()
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
        try {
            return HttpClientMaker.get("https://api.kucoin.com/api/v3/currencies");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}