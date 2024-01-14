package com.example.backend_parser;

import com.example.backend_parser.calculations.SpreadCalculator;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.ExchangePair;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.service.TelegramService;
import com.example.backend_parser.splitter.Splitter;
import com.example.backend_parser.utils.BanListUtils;
import com.example.backend_parser.utils.BlackListUtils;
import com.example.backend_parser.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpreadFinder {

    private static final int MIN_SPREAD = 2;
    private static final int MAX_SPREAD = 20;

    public static void findSpreads() throws InterruptedException {
        Splitter.split();

        parseTokensAndSendSpread();
    }

    private static void parseTokensAndSendSpread() throws InterruptedException {
        List<String> exchangesNames = List.of();
        List<ExchangePair> exchangePairs = parseExchangesPairs(exchangesNames);

        for (ExchangePair pair : exchangePairs) {
            for (Token token : pair.exchange1.getTokens()) {
                for (Token token1 : pair.exchange2.getTokens()) {
                    if (token.getBase().equalsIgnoreCase(token1.getBase())) {
                        defineSpread(token, token1, pair.exchange1, pair.exchange2);
                    }
                }
            }
        }
    }

    private static void defineSpread(Token token1, Token token2, Exchange exchange1, Exchange exchange2)
            throws InterruptedException {
        double spread = SpreadCalculator.calculateSpread(token1, token2);
        if (spread > MIN_SPREAD && spread < MAX_SPREAD) {
            if (BlackListUtils.tokenInBlackList(token1, token2, exchange1, exchange2)
                    || BanListUtils.tokenInBanList(token1, exchange1)
                    || BanListUtils.tokenInBanList(token2, exchange2)) {
                return;
            }
            BlackListUtils.addToBlackList(token1.getSymbol(), exchange1.getBlackList());
            BlackListUtils.addToBlackList(token2.getSymbol(), exchange2.getBlackList());

            String formattedMessage = MessageUtils.getFormattedMessage(token1, token2, exchange1, exchange2, spread);
            if (token1.getBid() > token2.getAsk()) {
                formattedMessage = MessageUtils.getFormattedMessage(token2, token1, exchange2, exchange1, spread);
            }

            TelegramService.sendMessage(formattedMessage);
            Thread.sleep(1000);
        }
    }

    private static List<ExchangePair> parseExchangesPairs(List<String> blackExchangesList) {
        List<ExchangePair> exchangesList = new ArrayList<>();

        for (Exchange exchange1 : Splitter.exchanges) {
//            blackExchangesList.add(exchange1.getName());
            for (Exchange exchange2 : Splitter.exchanges) {
                if (blackExchangesList.contains(exchange2.getName())) {
                    continue;
                }
                exchangesList.add(new ExchangePair(exchange1, exchange2));
            }
        }

        return exchangesList;
    }
}