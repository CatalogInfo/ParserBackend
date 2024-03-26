package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.encoder.Base64Encoder;
import com.example.backend_parser.encoder.HexEncoder;
import com.example.backend_parser.encoder.HmacSha256Calculator;
import com.example.backend_parser.encoder.SignatureCalculator;
import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.OkxMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class OkxExchange extends BaseExchange {
    IExchangeService service = new ExchangeService(
            "https://www.okx.com/api/v5/market/books?instId=",
            "https://www.okx.com/api/v5/public/instruments?instType=SPOT",
            new OkxMapper(),
            "&sz=50"
    );

    private SignatureCalculator signatureCalculator = new HmacSha256Calculator(new Base64Encoder());

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

        String formattedDateTime = getTimestamp();

        String prehash = formattedDateTime + "GET" + "/api/v5/asset/currencies";
        String signature = "";
        try {
            signature = signatureCalculator.calculate(secretKey, prehash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("OK-ACCESS-KEY", apiKey);
        headers.put("OK-ACCESS-SIGN", signature);
        headers.put("OK-ACCESS-TIMESTAMP", formattedDateTime);
        headers.put("OK-ACCESS-PASSPHRASE", passphrase);
        String response = null;
        try {
            response = HttpClientMaker.get("https://www.okx.com/api/v5/asset/currencies");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    private static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }
}
