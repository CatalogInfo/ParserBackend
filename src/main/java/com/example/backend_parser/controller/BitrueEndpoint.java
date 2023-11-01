package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bitrue")
public class BitrueEndpoint extends BaseEndpoint {
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
}
