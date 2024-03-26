package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.parser.mapper.exchanges.LBankMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;

import java.io.IOException;

public class LBankExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.lbkex.com/v2/depth.do?symbol=",
            "https://api.lbkex.com/v2/currencyPairs.do",
            new LBankMapper(),
            "&size=100"
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

    public String requestChains() throws IOException {

        return null;
    }
}