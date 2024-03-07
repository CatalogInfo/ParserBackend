package com.example.backend_parser.service;

import com.example.backend_parser.logs.LogFactory;
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
import java.util.Random;
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
    private final int LOOPS = 8;
    final String USDT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7";
    List<ProxyWithApiToken> proxies = ReadProxiesService.readProxies("proxiesWithAPI.txt");
    ProxyWithApiToken proxyForDoubleCheckingPrice = new ProxyWithApiToken("138.128.148.94", 6654, "qoeqmlvv", "oydvaukmtb30", "hz0tmcc7FEzdVQc41C2wBq5qCfjivbaw", "0xb1c5f6831f78106687cf8ce5ee9b1085ca7ae55f");
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
    public Token parseOrderBookForToken(Token token, int minAmount) {
        processToken(token, proxyForDoubleCheckingPrice, minAmount);
        return token;
    }

    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {

        for (int i = 0; i < LOOPS; i++) {
            ExecutorService executorServiceAsks = Executors.newFixedThreadPool(10);

            for (int j = 0; j < proxies.size(); j ++) {
                int finalTokenNumber = i * proxies.size() + j;

                int finalJ = j;
                executorServiceAsks.submit(() -> {
                    processAsk(tokens.get(finalTokenNumber), proxies.get(finalJ), minAmount);
                    LogFactory.makeALog(proxies.get(finalJ).getApiToken() + " Ask");
                });
            }
            executorServiceAsks.shutdown();
            try {
                executorServiceAsks.awaitTermination(3, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ExecutorService executorServiceBids = Executors.newFixedThreadPool(10);

            for (int j = 0; j < proxies.size(); j ++) {
                int finalTokenNumber = i * proxies.size() + j;

                if (finalTokenNumber == tokens.size() - 1) {
                    List<Token> finalTokens = tokens;
                    clearInnerData();

                    LogFactory.makeALog("Ending termination");
                    return finalTokens;
                }

                int finalJ = j;
                executorServiceBids.submit(() -> {
                    processBid(tokens.get(finalTokenNumber), proxies.get(finalJ));
                    LogFactory.makeALog(proxies.get(finalJ).getApiToken() + " Bid");
                });
            }

            executorServiceBids.shutdown();
            try {
                executorServiceBids.awaitTermination(3, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LogFactory.makeALog("Waiting start");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogFactory.makeALog("Waiting end");
        }

        return tokens;
    }

    private void processToken(Token token, ProxyWithApiToken proxyWithApiToken, double minAmount) {
        PriceAmount ask = getAsk(token.getAddress(), (int) (minAmount * Math.pow(10, 6)), proxyWithApiToken, token.getDecimals());

        LogFactory.makeALog(proxyWithApiToken.getApiToken() + " Ask");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PriceAmount bid = getBid(token.getAddress(), ask.getAmount(), proxyWithApiToken, token.getDecimals());
        LogFactory.makeALog(proxyWithApiToken.getApiToken() + " Bid");

        token.setAsk(ask.getPrice());
        token.setBid(bid.getPrice());

    }

    private void processAsk(Token token, ProxyWithApiToken proxyWithApiToken, double minAmount) {
        PriceAmount ask = getAsk(token.getAddress(), (int) (minAmount * Math.pow(10, 6)), proxyWithApiToken, token.getDecimals());
        token.setAsk(ask.getPrice());
        token.setAskAmount(ask);
    }
    private void processBid(Token token, ProxyWithApiToken proxyWithApiToken) {
        PriceAmount bid = getBid(token.getAddress(), token.getAskAmount().getAmount(), proxyWithApiToken, token.getDecimals());
        token.setBid(bid.getPrice());
    }
    public PriceAmount getBid(String addressFrom, String minAmountString, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), addressFrom, USDT_ADDRESS, minAmountString);
        JSONObject object = JsonUtils.getJSONObject(response);

        if(response.equals("Your account has been suspended")) {
            System.out.println("PIZDEC");
            return new PriceAmount(0, "0");
        }

        if(object.has("statusCode")) {
            return new PriceAmount(0, "0");
        }

        PriceAmount priceAmount = calculateFinalAmount(object, 6);
        BigDecimal divider = new BigDecimal("10").pow(decimals);
        BigDecimal amount = new BigDecimal(minAmountString).divide(divider);
        priceAmount.setPrice(calculateFinalPrice(Double.parseDouble(String.valueOf(amount)), priceAmount.getPrice()));

        return priceAmount;
    }
    public PriceAmount getAsk(String addressTo, int minAmount, ProxyWithApiToken proxyWithApiToken, int decimals) {
        String response = "";
        try {
            response = ProxyService.requestWithProxy(proxyWithApiToken, proxyWithApiToken.getApiToken(), USDT_ADDRESS, addressTo, String.valueOf(minAmount));
        } catch (Exception e) {
            e.printStackTrace();
        }   if(response.equals("Your account has been suspended")) {
            System.out.println("PIZDEC");
            return new PriceAmount(0, "0");
        }


        JSONObject object = JsonUtils.getJSONObject(response);

        if(object.has("statusCode")) {
            return new PriceAmount(0, "0");
        }
        PriceAmount priceAmount = calculateFinalAmount(object, decimals);
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

