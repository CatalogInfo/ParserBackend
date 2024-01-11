package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.BybitMapper;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.getSignatureHmac256;
import static com.example.backend_parser.request.RequestUtils.readFromConnection;

public class BybitExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.bybit.com/v5/market/orderbook?category=spot&symbol=",
            "https://api.bybit.com/v5/market/instruments-info?category=spot",
            new BybitMapper(),
            "&limit=50"
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
        String apiKey = "adFsWqsCxQLQbRkB30";
        String secretKey = "TpfvO7cfdEqjWBsW1trQbI9ul5gmuW3l34Ed";

        HttpClient httpclient = HttpClients.createDefault();
        String time = RequestMaker.getRequest("https://api.bybit.com/v3/public/time");
        JSONObject o = new JSONObject(time);

        String timestamp = String.valueOf(o.get("time"));
        HttpGet httpGet = new HttpGet("https://api.bybit.com/v5/asset/coin/query-info");
        httpGet.addHeader("X-BAPI-API-KEY", apiKey);
        httpGet.addHeader("X-BAPI-TIMESTAMP", timestamp);
        httpGet.addHeader("X-BAPI-RECV-WINDOW", "20000");
        httpGet.addHeader("X-BAPI-SIGN", getSignatureHmac256(secretKey, timestamp, apiKey, "20000"));

        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }

}
