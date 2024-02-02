package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.XTcomMapper;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.readFromConnection;

public class XTcomExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://sapi.xt.com/v4/public/depth?symbol=",
            "https://sapi.xt.com/v4/public/symbol",
            new XTcomMapper()
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
        HttpGet httpGet = new HttpGet("https://sapi.xt.com/v4/public/wallet/support/currency");

        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }
}
