package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.KrakenMapper;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class KrakenExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.kraken.com/0/public/Depth?pair=",
            "https://api.kraken.com/0/public/AssetPairs",
            new KrakenMapper()
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
