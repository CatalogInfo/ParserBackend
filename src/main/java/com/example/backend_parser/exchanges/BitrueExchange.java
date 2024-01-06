package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

public class BitrueExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://openapi.bitrue.com/api/v1/depth?symbol=",
            "https://openapi.bitrue.com/api/v1/exchangeInfo",
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

    @Override
    protected String getAuthToken() {
        return null;
    }
}
