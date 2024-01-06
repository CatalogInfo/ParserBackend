package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.GateMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;

public class GateExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.gateio.ws/api/v4/spot/order_book?currency_pair=",
            "https://api.gateio.ws/api/v4/spot/currency_pairs",
            new GateMapper()
    );
    @Override
    protected IExchangeService getService() {
        return service;
    }
    @Override
    protected int getDelayTime() {
        return 50;
    }
}
