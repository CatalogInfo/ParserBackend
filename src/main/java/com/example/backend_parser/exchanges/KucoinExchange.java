package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.KucoinMapper;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.getSignatureHmac256;
import static com.example.backend_parser.request.RequestUtils.readFromConnection;

public class KucoinExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.kucoin.com/api/v1/market/orderbook/level2_100?symbol=",
            "https://api.kucoin.com/api/v2/symbols",
            new KucoinMapper()
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
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.kucoin.com/api/v3/currencies");

        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }
}