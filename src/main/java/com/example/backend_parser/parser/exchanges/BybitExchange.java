package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.encoder.HexEncoder;
import com.example.backend_parser.encoder.HmacSha256Calculator;
import com.example.backend_parser.encoder.SignatureCalculator;
import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.BybitMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class BybitExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.bybit.com/v5/market/orderbook?category=spot&symbol=",
            "https://api.bybit.com/v5/market/instruments-info?category=spot",
            new BybitMapper(),
            "&limit=50"
    );
    private SignatureCalculator signatureCalculator = new HmacSha256Calculator(new HexEncoder());

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

        String time = null;
        try {
            time = HttpClientMaker.get("https://api.bybit.com/v3/public/time");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JSONObject o = new JSONObject(time);

        String timestamp = String.valueOf(o.get("time"));
        String queryStr = timestamp + apiKey + "20000";
        String signature = "";
        try {
            signature = signatureCalculator.calculate(secretKey, queryStr);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-BAPI-API-KEY", apiKey);
        headers.put("X-BAPI-TIMESTAMP", timestamp);
        headers.put("X-BAPI-RECV-WINDOW", "20000");
        headers.put("X-BAPI-SIGN", signature);

        String response = null;
        try {
            response = HttpClientMaker.get("https://api.bybit.com/v5/asset/coin/query-info", headers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

}
