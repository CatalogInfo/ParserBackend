package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.mapper.exchanges.OkxMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/okx")
public class OkxEndpoint extends BaseEndpoint {
    IExchangeService service = new ExchangeService(
            "https://www.okx.com/api/v5/market/books?instId=",
            "https://www.okx.com/api/v5/public/instruments?instType=SPOT",
            new OkxMapper(),
            "&sz=50"
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
