package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huobi")
public class HuobiEndpoint extends BaseEndpoint {
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
}
