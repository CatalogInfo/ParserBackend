package com.example.backend_parser.parser.exchanges;

import com.example.backend_parser.encoder.HexEncoder;
import com.example.backend_parser.encoder.HmacSha256Calculator;
import com.example.backend_parser.encoder.SignatureCalculator;
import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.parser.mapper.exchanges.BinanceMapper;
import com.example.backend_parser.parser.service.IExchangeService;
import com.example.backend_parser.parser.service.ExchangeService;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class BinanceExchange extends BaseExchange {
    private SignatureCalculator signatureCalculator = new HmacSha256Calculator(new HexEncoder());
    IExchangeService service = new ExchangeService(
                "https://api4.binance.com/api/v3/depth?symbol=",
                        "https://api4.binance.com/api/v3/exchangeInfo",
                        new BinanceMapper()
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
//        String apiKey = "JXMhc3A95RFyPyu9wHVNW4F1IezB4GGpPS04HPpuZAJS9oN7GwopQ8GT687tBJSo";
//        String secretKey = "NeoU0MTdouM4RsUpIMD3aTQKPAO10w09PX4FBC2NOKstd6nJng0L2fgsjqaRjvoG";
//
//        String time = RequestMaker.getRequest("https://api.binance.com/api/v3/time");
//
//        JSONObject o = new JSONObject(time);
//        String timeStamp = String.valueOf(o.get("serverTime"));
//        HttpClient httpclient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("https://api.binance.com/sapi/v1/capital/config/getall?timestamp="+timeStamp+ "&signature="+getSignatureHmac256("timestamp="+timeStamp, secretKey));
//        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        httpGet.addHeader("X-MBX-APIKEY", apiKey);
//        HttpResponse response = httpclient.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//        InputStream inputStream = entity.getContent();
//
//        return readFromConnection(inputStream);
        String apiKey = "JXMhc3A95RFyPyu9wHVNW4F1IezB4GGpPS04HPpuZAJS9oN7GwopQ8GT687tBJSo";
        String secretKey = "NeoU0MTdouM4RsUpIMD3aTQKPAO10w09PX4FBC2NOKstd6nJng0L2fgsjqaRjvoG";

        String time = null;
        try {
            time = HttpClientMaker.get("https://api.binance.com/api/v3/time", null, null);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JSONObject timeJson = new JSONObject(time);
        String timeStamp = String.valueOf(timeJson.get("serverTime"));
        String signature = null;
        try {
            signature = signatureCalculator.calculate("timestamp=" + timeStamp, secretKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        String url = "https://api.binance.com/sapi/v1/capital/config/getall?timestamp=" + timeStamp + "&signature=" + signature;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("X-MBX-APIKEY", apiKey);

        try {
            return HttpClientMaker.get(url, null, headers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}