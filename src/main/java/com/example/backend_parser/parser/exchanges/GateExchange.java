package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.encoder.HexEncoder;
import com.example.backend_parser.encoder.HmacSha256Calculator;
import com.example.backend_parser.encoder.HmacSha512Calculator;
import com.example.backend_parser.encoder.SignatureCalculator;
import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.GateMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class GateExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://api.gateio.ws/api/v4/spot/order_book?currency_pair=",
            "https://api.gateio.ws/api/v4/spot/currency_pairs",
            new GateMapper()
    );

    private SignatureCalculator signatureCalculator = new HmacSha512Calculator(new HexEncoder());

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

        String signature = null;
        try {
            signature = signatureCalculator.calculate(secretKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Key", apiKey);
        headers.put("Sign", signature);

        String response = null;
        try {
            response = HttpClientMaker.get("https://api.gateio.la/api2/1/private/feelist", headers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
