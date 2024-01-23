package com.example.backend_parser.service;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.ProxyService;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.JsonUtils;
import com.example.backend_parser.utils.ThreadUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class InchService extends Service {
    private final List<Thread> threads = new ArrayList<>();
    private final List<Token> tokens = new ArrayList<>();
    private final String tradingPairsUrl;
    private final String orderBookUrl;
    private final IMapper mapper;
    private String additionalUrlParams = "";
    final String USDT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7";
    List<ProxyWithApiToken> proxies = ReadProxiesService.readProxies("proxiesWithAPI.txt");

    public InchService(String orderBookUrl, String tradingPairsUrl, IMapper mapper) {
        this.orderBookUrl = orderBookUrl;
        this.tradingPairsUrl = tradingPairsUrl;
        this.mapper = mapper;
    }

    public InchService(String orderBookUrl, String tradingPairsUrl, IMapper mapper, String additionalUrlParams) {
        this.orderBookUrl = orderBookUrl;
        this.tradingPairsUrl = tradingPairsUrl;
        this.mapper = mapper;
        this.additionalUrlParams = additionalUrlParams;
    }

    @Getter
    @Setter
    public static class PriceAmount {
        String amount;
        double price;

        public PriceAmount(int price, String amount) {
            this.price = price;
            this.amount = amount;
        }
        public PriceAmount() {

        }
    }

    @Override
    String getOrderBookUrl() {

        return this.orderBookUrl;
    }
    @Override
    String getTradingPairsUrl() {
        return this.tradingPairsUrl;
    }
    @Override
    String getAdditionalUrlParams() {
        return this.additionalUrlParams;
    }
    @Override
    IMapper getMapper() {
        return this.mapper;
    }
    @Override
    List<Thread> getThreads() {
        return this.threads;
    }
    @Override
    List<Token> getTokens() {
        return tokens;
    }


    @Override
    public List<Token> parseTradingPairs(String authToken) {
        IMapper mapper = getMapper();
        return mapper.convertBaseQuote(RequestMaker.getRequestWithAuth(getTradingPairsUrl(), authToken));
    }

    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {
        int tokenNumber = 0;
        for(int i = 0; i < 2; i ++) {
            for (ProxyWithApiToken proxyWithApiToken : proxies) {
                int finalTokenNumber = tokenNumber;
                if(finalTokenNumber == tokens.size() - 1) {
                    return tokens;
                }
                Thread t = new Thread(() -> {
                    PriceAmount ask = getAsk(tokens.get(finalTokenNumber).getAddress(), (int)(minAmount * Math.pow(10, 6)), proxyWithApiToken, tokens.get(finalTokenNumber).getDecimals());

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    PriceAmount bid = getBid(tokens.get(finalTokenNumber).getAddress(), ask.getAmount(), proxyWithApiToken, tokens.get(finalTokenNumber).getDecimals());


                    System.out.println("BID: " + bid.price + " " + tokens.get(finalTokenNumber).getAddress() + " " + tokens.get(finalTokenNumber).getSymbol());
                    System.out.println("ASK: " + ask.price + " " + tokens.get(finalTokenNumber).getAddress() + " " +  tokens.get(finalTokenNumber).getSymbol());

                    tokens.get(finalTokenNumber).setAsk(ask.getPrice());
                    tokens.get(finalTokenNumber).setBid(bid.getPrice());
                });
                tokenNumber ++;
                t.start();
                threads.add(t);
            }
            ThreadUtils.waitTillThreadsExecuted(threads);
            System.out.println("wait started");

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("wait ended");

        }

        return tokens;
    }

    public PriceAmount getBid(String addressFrom, String minAmountString, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), addressFrom, USDT_ADDRESS, minAmountString);
        JSONObject object = JsonUtils.getJSONObject(response);

        if(object.has("statusCode") && object.getInt("statusCode") == 400) {
            return new PriceAmount(0, "0");
        }
        PriceAmount priceAmount = calculateFinalAmount(object, 6);

        BigDecimal divider = new BigDecimal("10").pow(decimals);

        BigDecimal amount = new BigDecimal(minAmountString).divide(divider);
        double price = calculateFinalPrice(Double.parseDouble(String.valueOf(amount)), priceAmount.getPrice());
        JSONObject presets = object.getJSONObject("presets");
        JSONObject fast = presets.getJSONObject("fast");
        BigDecimal fee = new BigDecimal(fast.getString("tokenFee")).divide(divider);
        priceAmount.setPrice(calculateFinalPrice(Double.parseDouble(String.valueOf(amount)), priceAmount.getPrice()));
        if(addressFrom.equalsIgnoreCase("0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2")) {
            System.out.println("SASAT " + new BigDecimal(minAmountString) + " " + priceAmount.getPrice());
        }
        return priceAmount;
    }
    public PriceAmount getAsk(String addressTo, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = "";
        try {
            response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), USDT_ADDRESS, addressTo, String.valueOf(minAmount));
        } catch (Exception e) {
            System.out.println(proxyWithApiToken.getApiToken());
        }

        System.out.println(response);
        JSONObject object = JsonUtils.getJSONObject(response);

        if(object.has("statusCode") && object.getInt("statusCode") == 400) {
            return new PriceAmount(0, "0");
        }
        PriceAmount priceAmount = calculateFinalAmount(object, decimals);
        JSONObject presets = object.getJSONObject("presets");
        JSONObject fast = presets.getJSONObject("fast");
        BigDecimal divider = new BigDecimal("10").pow(decimals);

        BigDecimal fee = new BigDecimal(fast.getString("tokenFee")).divide(divider);
        priceAmount.setPrice(calculateFinalPrice(priceAmount.getPrice(), minAmount) / Math.pow(10, 6));
        return priceAmount;
    }

    private PriceAmount calculateFinalAmount(JSONObject obj, int decimals) {
        if(!obj.has("toTokenAmount")) {
            return new PriceAmount();
        }
        String toAmountString = String.valueOf(obj.get("toTokenAmount"));
        PriceAmount priceAmount = new PriceAmount();

        double toAmount = Double.parseDouble(toAmountString);

        priceAmount.setAmount(toAmountString);
        priceAmount.setPrice(toAmount / Math.pow(10, decimals));
        return priceAmount;
    }

    private double calculateFinalPrice(double amountFrom, double amountTo) {
        return amountTo / amountFrom;
    }
}

