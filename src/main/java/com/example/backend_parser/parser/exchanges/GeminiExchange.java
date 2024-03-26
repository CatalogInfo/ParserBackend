package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.BitgetMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;

import java.io.IOException;
public class GeminiExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.gemini.com/v1/book/",
            "https://api.bitget.com/api/v2/spot/public/symbols",
            new BitgetMapper()
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
            return HttpClientMaker.get("https://api.bitget.com/api/v2/spot/public/coins", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}