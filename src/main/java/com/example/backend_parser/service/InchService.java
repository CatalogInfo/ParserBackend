package com.example.backend_parser.service;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.ProxyService;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.JsonUtils;
import com.example.backend_parser.utils.ThreadUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        for(int i = 0; i < 10; i ++) {
            for (ProxyWithApiToken proxyWithApiToken : proxies) {

                int finalTokenNumber = tokenNumber;
                Thread t = new Thread(() -> {
                    PriceAmount ask = getAsk(tokens.get(finalTokenNumber).getAddress(), (int)(minAmount * Math.pow(10, 6)), proxyWithApiToken, tokens.get(finalTokenNumber).getDecimals());

                    try {
                        Thread.sleep(1000);
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
                Thread.sleep(2000);
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
        PriceAmount priceAmount = calculateFinalAmount(object, 6);

        BigInteger divider = new BigInteger("10").pow(decimals);

        BigInteger amount = new BigInteger(minAmountString).divide(divider);
        double price = calculateFinalPrice(Double.parseDouble(String.valueOf(amount)), priceAmount.getPrice());
        JSONObject presets = object.getJSONObject("presets");
        JSONObject fast = presets.getJSONObject("fast");
        BigInteger fee = new BigInteger(fast.getString("tokenFee")).divide(divider);
        priceAmount.setPrice(calculateFinalPrice(Double.parseDouble(String.valueOf(amount)), priceAmount.getPrice()));
        return priceAmount;
    }
    public PriceAmount getAsk(String addressTo, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), USDT_ADDRESS, addressTo, String.valueOf(minAmount));
        JSONObject object = JsonUtils.getJSONObject(response);
        PriceAmount priceAmount = calculateFinalAmount(object, decimals);
        JSONObject presets = object.getJSONObject("presets");
        JSONObject fast = presets.getJSONObject("fast");
        BigInteger divider = new BigInteger("10").pow(decimals);

        BigInteger fee = new BigInteger(fast.getString("tokenFee")).divide(divider);
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

