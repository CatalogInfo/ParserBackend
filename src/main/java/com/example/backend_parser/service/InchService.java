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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        return getMapper().convertBaseQuote(RequestMaker.getRequestWithAuth(getTradingPairsUrl(), authToken));
    }

    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {
        int tokenNumber = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 2; i++) {
            for (ProxyWithApiToken proxyWithApiToken : proxies) {
                int finalTokenNumber = tokenNumber;

                if (finalTokenNumber == tokens.size() - 1) {
                    List<Token> finalTokens = tokens;
                    System.out.println(finalTokens.size() + " JJJJJJJJJJJJJJJJJJJJJJ");
                    clearInnerData();

                    executorService.shutdown();
                    try {
                        executorService.awaitTermination(3, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("ENDED execution inner");

                    return finalTokens;
                }

                executorService.submit(() -> processToken(tokens.get(finalTokenNumber), proxyWithApiToken, minAmount));

                tokenNumber++;
            }

            System.out.println("Waiting started");

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Waiting ended");
        }

        return tokens;
    }

    private void processToken(Token token, ProxyWithApiToken proxyWithApiToken, double minAmount) {
        PriceAmount ask = getAsk(token.getAddress(), (int) (minAmount * Math.pow(10, 6)), proxyWithApiToken, token.getDecimals());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PriceAmount bid = getBid(token.getAddress(), ask.getAmount(), proxyWithApiToken, token.getDecimals());

        token.setAsk(ask.getPrice());
        token.setBid(bid.getPrice());

        if(token.getBase().equals("STMX")) {
            System.out.println("BID: " + token.getBid() + " " + token.getAddress() + " " + token.getSymbol());
            System.out.println("ASK: " + token.getAsk() + " " + token.getAddress() + " " + token.getSymbol());

        }



    }
    public PriceAmount getBid(String addressFrom, String minAmountString, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), addressFrom, USDT_ADDRESS, minAmountString);
        JSONObject object = JsonUtils.getJSONObject(response);

        if(object.has("statusCode")) {
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

        return priceAmount;
    }
    public PriceAmount getAsk(String addressTo, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = "";
        try {
            response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), USDT_ADDRESS, addressTo, String.valueOf(minAmount));
        } catch (Exception e) {
            System.out.println(proxyWithApiToken.getApiToken());
        }

        JSONObject object = JsonUtils.getJSONObject(response);

        if(object.has("statusCode")) {
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

    private void clearInnerData() {
        getThreads().clear();
        getTokens().clear();
    }
}

