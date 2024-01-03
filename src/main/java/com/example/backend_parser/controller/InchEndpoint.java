package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inch")
public class InchEndpoint extends BaseEndpoint {
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
}
