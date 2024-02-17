package com.example.backend_parser.splitter;

import com.example.backend_parser.dtos.OptionsDto;
import com.example.backend_parser.entities.Options;
import com.example.backend_parser.exchanges.*;
import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.utils.ThreadUtils;
import com.google.gson.Gson;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Splitter {
    public static List<Exchange> exchanges = new ArrayList<>();

    public static OptionsDto options;
    public static int loopNumber = 0;
    public static void init() {
        options = getOptions();

//        exchanges.add(new Exchange("binance", "https://www.binance.com/en/trade/", "", "", new BinanceExchange())); // doesn't matter
        exchanges.add(new Exchange("gate", "https://www.gate.io/trade/", "_", "_", new GateExchange())); // BASE_QUOTE, ++
//        exchanges.add(new Exchange("bitrue", "https://www.bitrue.com/trade/", "", "_", new BitrueExchange())); // BASEQOUTE api, ++
//        exchanges.add(new Exchange("okx", "https://www.okx.com/trade-spot/", "-", "-", new OkxExchange())); // BASE-QUOTE api, ++
//        exchanges.add(new Exchange("huobi", "https://www.htx.com/en-us/trade/", "_", "_", true, new HuobiExchange(), "?type=spot")); // basequote api, base_quote link dolboebi
        exchanges.add(new Exchange("bybit", "https://www.bybit.com/en-US/trade/spot/", "", "/", new BybitExchange())); // BASEQUOTE , BASE/QUOTE link eblan
//        exchanges.add(new Exchange("1inch", "https://app.1inch.io/#/1/advanced/swap/", "/", "/", new InchExchange()));
//        exchanges.add(new Exchange("bitget", "https://www.bitget.com/ru/spot/", "", "", new BitgetExchange())); // BASEQOUTE api, ++
//        exchanges.add(new Exchange("xtcom", "https://www.xt.com/en/trade/", "_", "_", new XTcomExchange())); // BASEQOUTE api, ++
//        exchanges.add(new Exchange("kucoin", "kucoin.com/ru/trade/", "-", "-", new KucoinExchange())); // BASEQOUTE api, ++
//        exchanges.add(new Exchange("lbank", "https://www.lbank.com/trade/", "_", "_", new LBankExchange()));

//        exchanges.add(new Exchange("mexc", "", "", "", new MexcExchange()));
//        exchanges.add(new Exchange("kraken", "", "https://pro.kraken.com/app/trade/", "", new KrakenExchange())); // BASEQUOTE , BASE/QUOTE link eblani

    }

    public static void split() {
        loopNumber++;
        LogFactory.makeALog("Loop number -" + loopNumber + "- has started ");

        List<List<Token>> arrayOfPairs = new ArrayList<>();
        for (Exchange exchange : exchanges) {
            exchange.getBaseQuotes();
            arrayOfPairs.add(exchange.getTokens());
        }
        List<List<Token>> outputPairs = findRepeatedBaseAndQuoteElements(arrayOfPairs);
        LogFactory.makeALog("Base and Quotes parsed");


        for (int i = 0; i < exchanges.size(); i++) {
            exchanges.get(i).setTokens(outputPairs.get(i));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(exchanges.size());

        for (Exchange exchange : exchanges) {
            executorService.execute(() -> exchange.getOrderBook(exchange.getTokens()));
        }

        LogFactory.makeALog("Order books parsed");

        LogFactory.makeALog("  -- Starting waiting termination");
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LogFactory.makeALog("  --  Ending waiting termination");

        executorService = Executors.newFixedThreadPool(exchanges.size());

        for (Exchange exchange : exchanges) {
            executorService.execute(exchange::getChains);
        }
        LogFactory.makeALog("Chains parsed parsed");


        LogFactory.makeALog("  -- Starting waiting termination");
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LogFactory.makeALog("  --  Ending waiting termination");
        sendDataToWebsocket();
    }

    public static List<List<Token>> findRepeatedBaseAndQuoteElements(List<List<Token>> listOfLists) {
        Map<String, Long> baseCounts = listOfLists.stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Token::getBase, Collectors.counting()));

        return listOfLists.stream()
                .map(list -> list.stream()
                        .filter(token -> baseCounts.get(token.getBase()) > 1)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private static void sendDataToWebsocket() {
        RequestMaker.getRequest("http://localhost:8080/socket/exchanges");
    }

    private static OptionsDto getOptions() {
        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        OptionsDto options = gson.fromJson(RequestMaker.getRequest("http://localhost:8080/options"), OptionsDto.class);
        return options;
    }
}
