package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;

public class InchExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.1inch.dev/swap/v5.2/1/quote",
            "https://api.1inch.dev/token/v1.2/1",
            new HuobiMapper()
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
        return "B9PcyyN0hdb1u681DJm15oHpPaMllexg";
    }
}
