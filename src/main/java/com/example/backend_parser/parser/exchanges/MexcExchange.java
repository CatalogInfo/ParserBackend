package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.MexcMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;

import java.io.IOException;

public class MexcExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.mexc.com/api/v3/depth?symbol=",
            "https://api.mexc.com/api/v3/exchangeInfo",
            new MexcMapper()
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
            return HttpClientMaker.get("https://www.mexc.com/open/api/v2/market/coin/list", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
