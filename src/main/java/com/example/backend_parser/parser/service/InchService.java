package com.example.backend_parser.parser.service;

import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.parser.mapper.base.IMapper;
import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.proxy.ProxyModel;
import com.example.backend_parser.request.ProxiedRequestMaker;
import com.example.backend_parser.utils.JsonUtils;
import com.example.backend_parser.utils.ReadProxiesUtils;
import com.example.backend_parser.utils.ThreadUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
    private final int LOOPS = 8;
    private final int PROXIES_COULD_BE_USED = 2;

    final String USDT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7";
    List<ProxyWithApiToken> proxies = ReadProxiesUtils.readProxies("proxiesWithAPI.txt");
    ProxyWithApiToken proxyForDoubleCheckingPrice = new ProxyWithApiToken(new ProxyModel("45.151.163.74", 5827, "qoeqmlvv", "oydvaukmtb30"), "kshEcZA9EOJVajOrHFRXHjwF4edWCRB7", "0x6cf1624862136c7aa1355ca403613960313e92d7");
    ProxyWithApiToken proxyForTradingParsing = new ProxyWithApiToken(new ProxyModel("45.151.163.74", 5827, "qoeqmlvv", "oydvaukmtb30"), "kshEcZA9EOJVajOrHFRXHjwF4edWCRB7", "0x6cf1624862136c7aa1355ca403613960313e92d7");

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
        return getMapper().convertBaseQuote(ProxiedRequestMaker.makeRequestWithProxy(getTradingPairsUrl(), proxyForTradingParsing));
    }

    @Override
    public Token parseOrderBookForToken(Token token, int minAmount) {
        processToken(token, proxyForDoubleCheckingPrice, minAmount);
        return token;
    }

    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {

        for (int i = 0; i < LOOPS; i++) {
            ArrayList<ProxyWithApiToken> proxyWithApiTokensCopyBids = new ArrayList<>(proxies);

            ExecutorService executorServiceAsks = Executors.newFixedThreadPool(proxies.size());

            for (int j = 0; j < PROXIES_COULD_BE_USED; j ++) {
                int finalTokenNumber = i * PROXIES_COULD_BE_USED + j;
                System.out.println(proxyWithApiTokensCopyBids.size());
                Collections.shuffle(proxyWithApiTokensCopyBids);
                for (ProxyWithApiToken proxy : proxyWithApiTokensCopyBids) {
                    proxyWithApiTokensCopyBids.remove(proxy);

                    executorServiceAsks.submit(() -> {
                        processAsk(tokens.get(finalTokenNumber), proxy, minAmount);
                        LogFactory.makeALog(proxy.getApiToken() + " Ask");
                    });
                    break;
                }
            }
            executorServiceAsks.shutdown();
            try {
                executorServiceAsks.awaitTermination(100, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ThreadUtils.sleepOnRandomTimeBetween(2000, 3400);

            ExecutorService executorServiceBids = Executors.newFixedThreadPool(proxies.size());
            ArrayList<ProxyWithApiToken> proxyWithApiTokensCopyAsks = new ArrayList<>(proxies);

            for (int j = 0; j < PROXIES_COULD_BE_USED; j ++) {
                int finalTokenNumber = i * PROXIES_COULD_BE_USED + j;

                if (finalTokenNumber == tokens.size() - 1) {
                    List<Token> finalTokens = tokens;
                    clearInnerData();

                    LogFactory.makeALog("Ending termination");
                    return finalTokens;
                }

                Collections.shuffle(proxyWithApiTokensCopyAsks);

                for (ProxyWithApiToken proxy : proxyWithApiTokensCopyAsks) {
                    proxyWithApiTokensCopyAsks.remove(proxy);

                    executorServiceBids.submit(() -> {
                        processBid(tokens.get(finalTokenNumber), proxy);
                        LogFactory.makeALog(proxy.getApiToken() + " Bid");
                    });
                    break;
                }
            }

            executorServiceBids.shutdown();
            try {
                executorServiceBids.awaitTermination(100, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LogFactory.makeALog("Waiting start");
            ThreadUtils.sleepOnRandomTimeBetween(50000, 62112);
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
        String url = createUrl(addressFrom, USDT_ADDRESS, minAmountString, proxyWithApiToken.getWallet());
        String response = ProxiedRequestMaker.makeRequestWithProxy(url, proxyWithApiToken);

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
        String url = createUrl(USDT_ADDRESS, addressTo, String.valueOf(minAmount), proxyWithApiToken.getWallet());
        String response = ProxiedRequestMaker.makeRequestWithProxy(url, proxyWithApiToken);

        if(response.equals("Your account has been suspended")) {
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

    private String createUrl(String src, String dst, String amount, String walletAddress) {
        return "https://api.1inch.dev/fusion/quoter/v1.0/1/quote/receive?fromTokenAddress=" + src + "&toTokenAddress=" + dst + "&amount=" + amount + "&walletAddress=" + walletAddress + "&enableEstimate=true&isLedgerLive=true";
    }

    private double calculateFinalPrice(double amountFrom, double amountTo) {
        return amountTo / amountFrom;
    }

    private void clearInnerData() {
        getThreads().clear();
        getTokens().clear();
    }
}
