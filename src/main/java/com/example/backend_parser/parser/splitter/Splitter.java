package com.example.backend_parser.parser.splitter;

import com.example.backend_parser.api.dtos.OptionsDto;
import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.parser.ApiCommunicator;
import com.example.backend_parser.parser.exchanges.BinanceExchange;
import com.example.backend_parser.parser.exchanges.BybitExchange;
import com.example.backend_parser.parser.exchanges.GateExchange;
import com.example.backend_parser.parser.exchanges.InchExchange;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Splitter {
    public static List<Exchange> exchanges = new ArrayList<>();
    public static OptionsDto options;
    public static int loopNumber = 0;
    public static String parsingTime = "";
    public static ApiCommunicator apiCommunicator = new ApiCommunicator();
    public static void init() {
        options = getOptions();
        System.out.println(options);

        exchanges.add(new Exchange("binance", "https://www.binance.com/en/trade/", "", "", new BinanceExchange())); // doesn't matter
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
        long startTime = System.currentTimeMillis();

        loopNumber++;
        LogFactory.makeALog("Loop number -" + loopNumber + "- has started ");

        parseBaseAndQuotes();

        ExecutorService executorService = Executors.newFixedThreadPool(exchanges.size());

        parseOrderBooks(executorService);
        terminate(executorService);

        ExecutorService executorService1 = Executors.newFixedThreadPool(exchanges.size());

        parseChains(executorService1);
        terminate(executorService1);

        parsingTime = findOutExecutionTime(startTime);
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
        apiCommunicator.sendDataToClient();
    }

    private static OptionsDto getOptions() {
        return apiCommunicator.getOptions();
    }

    private static void parseBaseAndQuotes() {
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
    }

    private static void parseOrderBooks(ExecutorService executorService) {
        for (Exchange exchange : exchanges) {
            executorService.execute(() -> exchange.getOrderBook(exchange.getTokens()));
        }

        LogFactory.makeALog("Order books parsed");
    }

      private static void parseChains(ExecutorService executorService) {
        for (Exchange exchange : exchanges) {
            executorService.execute(exchange::getChains);
        }
        LogFactory.makeALog("Chains parsed parsed");
    }

    private static void terminate(ExecutorService pool) {
        LogFactory.makeALog("  -- Starting waiting termination");

        pool.shutdown(); // Disable new tasks from being submitted
        try {
            pool.awaitTermination(100, TimeUnit.MINUTES);//minutes means seconds(Library exception)
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        LogFactory.makeALog("  --  Ending waiting termination");
    }

    private static String findOutExecutionTime(long startTime) {
        long endTime = System.currentTimeMillis();

        long elapsedTimeMillis = endTime - startTime;

        long minutes = elapsedTimeMillis / (60 * 1000);
        long seconds = (elapsedTimeMillis / 1000) % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}