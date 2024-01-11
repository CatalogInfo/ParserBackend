package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.GateMapper;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.example.backend_parser.request.RequestUtils.*;

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
    @Override
    protected String getAuthToken() {
        return null;
    }
    public String requestChains() throws IOException {
        String apiKey = "585035BB-C501-4980-8281-EE1011E8B8EA";
        String secretKey = "1ce779cc5b9d7426cad72a970e38a2343552a0e33a72baea5b00224f76b39fca";

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.gateio.la/api2/1/private/feelist");
        httpPost.addHeader("Key", apiKey);
        httpPost.addHeader("Sign", getSignatureHmac512(secretKey));
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }
}
