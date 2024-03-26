package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.BitrueMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;

import java.io.IOException;

public class BitrueExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://openapi.bitrue.com/api/v1/depth?symbol=",
            "https://openapi.bitrue.com/api/v1/exchangeInfo",
            new BitrueMapper()
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
            return HttpClientMaker.get("https://openapi.bitrue.com/api/v1/exchangeInfo", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
