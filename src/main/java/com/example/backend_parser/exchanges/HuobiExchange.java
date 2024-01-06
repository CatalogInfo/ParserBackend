package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.springframework.web.bind.annotation.*;


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
}
