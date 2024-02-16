package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.BinanceMapper;
import com.example.backend_parser.mapper.exchanges.LBankMapper;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.service.ExchangeService;
import com.example.backend_parser.service.IExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.getSignatureHmac256;
import static com.example.backend_parser.request.RequestUtils.readFromConnection;

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
        String apiKey = "JXMhc3A95RFyPyu9wHVNW4F1IezB4GGpPS04HPpuZAJS9oN7GwopQ8GT687tBJSo";
        String secretKey = "NeoU0MTdouM4RsUpIMD3aTQKPAO10w09PX4FBC2NOKstd6nJng0L2fgsjqaRjvoG";

        String time = RequestMaker.getRequest("https://api.binance.com/api/v3/time");

        JSONObject o = new JSONObject(time);
        String timeStamp = String.valueOf(o.get("serverTime"));
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.binance.com/sapi/v1/capital/config/getall?timestamp="+timeStamp+ "&signature="+getSignatureHmac256("timestamp="+timeStamp, secretKey));
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpGet.addHeader("X-MBX-APIKEY", apiKey);
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }
}