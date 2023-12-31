package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.exchanges.BybitMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bybit")
public class BybitEndpoint extends BaseEndpoint {
    IExchangeService service = new ExchangeService(
            "https://api.bybit.com/v5/market/orderbook?category=spot&symbol=",
            "https://api.bybit.com/v5/market/instruments-info?category=spot",
            new BybitMapper(),
            "&limit=50"
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
