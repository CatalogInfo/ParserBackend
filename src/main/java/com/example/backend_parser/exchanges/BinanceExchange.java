package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;

public class BinanceExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
                "https://api4.binance.com/api/v3/depth?symbol=",
                        "https://api4.binance.com/api/v3/exchangeInfo",
                        new Mapper()
        );
    @Override
    protected IExchangeService getService() {
        return service;
    }

    @Override
    protected int getDelayTime() {
        return 100;
    }
}