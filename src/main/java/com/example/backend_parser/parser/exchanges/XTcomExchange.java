package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.XTcomMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;

import java.io.IOException;


public class XTcomExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://sapi.xt.com/v4/public/depth?symbol=",
            "https://sapi.xt.com/v4/public/symbol",
            new XTcomMapper()
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
            return HttpClientMaker.get("https://sapi.xt.com/v4/public/wallet/support/currency", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
