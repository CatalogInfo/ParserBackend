package com.example.backend_parser.splitter;

import com.example.backend_parser.exchanges.*;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.ThreadUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Splitter {
    public static List<Exchange> exchanges = new ArrayList<>();

    public static void init() {
        exchanges.add(new Exchange("binance", "https://www.binance.com/en/trade/", "", "", new BinanceExchange())); // doesn't matter
        exchanges.add(new Exchange("gate", "https://www.gate.io/trade/", "_", "_", new GateExchange())); // BASE_QUOTE, ++
        exchanges.add(new Exchange("bitrue", "https://www.bitrue.com/trade/", "", "_", new BitrueExchange())); // BASEQOUTE api, ++
        exchanges.add(new Exchange("okx", "https://www.okx.com/trade-spot/", "-", "-", new OkxExchange())); // BASE-QUOTE api, ++
        exchanges.add(new Exchange("huobi", "https://www.htx.com/en-us/trade/", "_", "_", true, new HuobiExchange(), "?type=spot")); // basequote api, base_quote link dolboebi
        exchanges.add(new Exchange("bybit", "https://www.bybit.com/en-US/trade/spot/", "", "/", new BybitExchange())); // BASEQUOTE , BASE/QUOTE link eblan

//        exchanges.add(new Exchange("1inch", "", "https://www.bybit.com/en-US/trade/spot/", "/", new InchExchange()));
//        exchanges.add(new Exchange("mexc", "", "", "", new MexcExchange()));
//        exchanges.add(new Exchange("kraken", "", "https://pro.kraken.com/app/trade/", "", new KrakenExchange())); // BASEQUOTE , BASE/QUOTE link eblani


    }

    public static void split() {
        System.out.println("Has started");

        List<List<Token>> arrayOfPairs = new ArrayList<>();
        for (Exchange exchange : exchanges) {
            exchange.getBaseQuotes();

            arrayOfPairs.add(exchange.getTokens());
        }
        List<List<Token>> outputPairs = findRepeatedBaseAndQuoteElements(arrayOfPairs);

        for (int i = 0; i < exchanges.size(); i++) {
            exchanges.get(i).setTokens(outputPairs.get(i));
        }

        List<Thread> threads = new ArrayList<>();

        for (Exchange exchange : exchanges) {

            Thread t = new Thread(() -> {
                exchange.getOrderBook(exchange.getTokens());
            });

            t.start();
            threads.add(t);
        }

        for (Exchange exchange : exchanges) {

            Thread t = new Thread(exchange::getChains);

            t.start();
            threads.add(t);
        }

        ThreadUtils.waitTillThreadsExecuted(threads);
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
}
