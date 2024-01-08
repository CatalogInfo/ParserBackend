package com.example.backend_parser.service;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONObject;

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
    List<ProxyWithApiToken> proxies = new ArrayList<>();

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

    private void initProxies() {
        proxies.add(new ProxyWithApiToken( "185.75.132.77", 8000, "8uJsED", "1qVb7f", "Q8bcWnHcvawk6cUKXIimjADeoT7154Vu"));
        proxies.add(new ProxyWithApiToken( "45.147.31.243", 8000, "HPvEDX", "Nqu3XC", "FsJEnnGnpvQ46ogwMPCFW0x5YGuuVqJY"));
    }

    @Override
    public List<Token> parseTradingPairs(String authToken) {
        IMapper mapper = getMapper();
        return mapper.convertBaseQuote(RequestMaker.getRequestWithAuth(getTradingPairsUrl(), authToken));
    }

    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {
        initProxies();
        int tokenNumber = 0;
        System.out.println("Entered ");
        for(int i = 0; i < tokens.size() / proxies.size(); i ++) {
            System.out.println("Entered " + tokens.get(tokenNumber));
            for (ProxyWithApiToken proxyWithApiToken : proxies) {

                int finalTokenNumber = tokenNumber;
                new Thread(() -> {
                    double ask = getAsk(tokens.get(finalTokenNumber).getAddress(), (int)(minAmount * Math.pow(10, 6)), proxyWithApiToken, tokens.get(finalTokenNumber).getDecimals());

                    System.out.println("AAAAAAAAAAAAAAAAAA");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    double bid = getBid(tokens.get(finalTokenNumber).getAddress(), (int)(minAmount * Math.pow(10, 6)), proxyWithApiToken, 6);
                    System.out.println("Bid:" + bid + " " + tokens.get(finalTokenNumber).getSymbol() + " " + tokens.get(finalTokenNumber).getAddress());
                    System.out.println("ASK:" + ask);
                    tokens.get(finalTokenNumber).setAsk(ask);
                    tokens.get(finalTokenNumber).setBid(bid);
                }).start();
                tokenNumber ++;
            }
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return tokens;
    }

    public double getAsk(String addressFrom, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        System.out.println("get ask workes");

        // request to proxy server
        String response = RequestMaker.inchQuoteRequest("https://api.1inch.dev/swap/v5.2/1/quote", proxyWithApiToken.getApiToken(), addressFrom, USDT_ADDRESS, (int)(minAmount *  Math.pow(10, decimals)));
        JSONObject object = JsonUtils.getJSONObject(response);
        double amountTo = calculateFinalAmount(object, decimals);
        System.out.println((int)(minAmount *  Math.pow(10, decimals)));
        double amountFrom = minAmount *  Math.pow(10, decimals);
        return calculateFinalPrice(amountFrom, amountTo);
    }
    public double getBid(String addressTo, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        System.out.println("get bid workes");
        // request to proxy server
        String response = RequestMaker.inchQuoteRequest("https://api.1inch.dev/swap/v5.2/1/quote", proxyWithApiToken.getApiToken(), USDT_ADDRESS, addressTo, (int)(minAmount *  Math.pow(10, decimals)));
        JSONObject object = JsonUtils.getJSONObject(response);
        double amountFrom = calculateFinalAmount(object, decimals);
        double amountTo = minAmount *  Math.pow(10, decimals);
        return calculateFinalPrice(amountFrom, amountTo);
    }

    private double calculateFinalAmount(JSONObject obj, int decimals) {
        if(!obj.has("toAmount")) {
            return 0;
        }
        String toAmountString = String.valueOf(obj.get("toAmount"));
        double toAmount = Double.parseDouble(toAmountString);
        return toAmount / Math.pow(10, decimals);
    }

    private double calculateFinalPrice(double amountFrom, double amountTo) {
        return amountTo / amountFrom;
    }
}

