package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.BitgetMapper;
import com.example.backend_parser.parser.service.ExchangeService;
import com.example.backend_parser.parser.service.IExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;


public class BitgetExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.bitget.com/api/v2/spot/market/orderbook?symbol=",
            "https://api.bitget.com/api/v2/spot/public/symbols",
            new BitgetMapper(),
            "&type=step0&limit=100"
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
        try {
            return HttpClientMaker.get("https://api.bitget.com/api/v2/spot/public/coins", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
