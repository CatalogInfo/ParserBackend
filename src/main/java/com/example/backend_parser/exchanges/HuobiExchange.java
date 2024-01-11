package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.readFromConnection;


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
    public String requestChains() throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.huobi.pro/v2/reference/currencies");

        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }
}
