package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.parser.mapper.exchanges.KrakenMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;

import java.io.IOException;

public class KrakenExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.kraken.com/0/public/Depth?pair=",
            "https://api.kraken.com/0/public/AssetPairs",
            new KrakenMapper()
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
    @Override
    public String requestChains() throws IOException {
        return null;
    }
}
