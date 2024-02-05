package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.InchMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.InchService;

import java.io.IOException;

public class InchExchange extends BaseExchange {
    IExchangeService service = new InchService(
            "",
            "https://api.1inch.dev/token/v1.2/1",
            new InchMapper()
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
        return "Q8bcWnHcvawk6cUKXIimjADeoT7154Vu";
    }

    @Override
    public String requestChains() throws IOException {
        return null;
    }
}
