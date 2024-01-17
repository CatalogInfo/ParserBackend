package com.example.backend_parser.exchanges;

import com.example.backend_parser.mapper.exchanges.OkxMapper;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.service.IExchangeService;
import com.example.backend_parser.service.ExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.backend_parser.request.RequestUtils.getSignatureHmac256;
import static com.example.backend_parser.request.RequestUtils.readFromConnection;

public class OkxExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://www.okx.com/api/v5/market/books?instId=",
            "https://www.okx.com/api/v5/public/instruments?instType=SPOT",
            new OkxMapper(),
            "&sz=50"
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
        String apiKey = "dd3865c4-0b0b-4e78-9502-53557d22c2da";
        String secretKey = "645C667BE8B5B59F5F3871E66EF922CC";
        String passphrase = "Max1357924680*";

        HttpClient httpclient = HttpClients.createDefault();

        String formattedDateTime = getTimestamp();
        HttpGet httpGet = new HttpGet("https://www.okx.com/api/v5/asset/currencies");
        httpGet.addHeader("OK-ACCESS-KEY", apiKey);
        httpGet.addHeader("OK-ACCESS-SIGN", getSignatureHmac256(secretKey, formattedDateTime, "/api/v5/asset/currencies"));
        httpGet.addHeader("OK-ACCESS-TIMESTAMP", formattedDateTime);
        httpGet.addHeader("OK-ACCESS-PASSPHRASE", passphrase);

        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        return readFromConnection(inputStream);
    }

    private static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }
}
