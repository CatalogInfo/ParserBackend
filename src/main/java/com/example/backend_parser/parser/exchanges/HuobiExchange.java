package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;

import java.io.IOException;


public class HuobiExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.huobi.pro/market/depth?symbol=",
            "https://api.huobi.pro/v2/settings/common/symbols",
            new HuobiMapper(),
            "&type=step0"
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
            return HttpClientMaker.get("https://api.huobi.pro/v2/reference/currencies", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
