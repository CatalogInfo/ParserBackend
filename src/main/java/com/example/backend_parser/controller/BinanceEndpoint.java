package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/binance")
public class BinanceEndpoint extends BaseEndpoint {
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