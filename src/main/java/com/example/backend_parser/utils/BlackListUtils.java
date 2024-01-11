package com.example.backend_parser.utils;

import com.example.backend_parser.models.BlackListItem;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;

import java.util.List;

public class BlackListUtils {

    public static boolean tokenInBlackList(Token token1, Token token2, Exchange exchange1, Exchange exchange2) {
        return hasTokenBySymbolFromBlackList(token1.getSymbol(), exchange1.getBlackList())
                && hasTokenBySymbolFromBlackList(token2.getSymbol(), exchange2.getBlackList())
                && !isTokenReady(token1.getSymbol(), token2.getSymbol(), exchange1, exchange2);
    }

    public static void addToBlackList(String symbol, List<BlackListItem> blackList) {
        if (hasTokenBySymbolFromBlackList(symbol, blackList)) {
            return;
        }

        blackList.add(new BlackListItem(symbol, System.currentTimeMillis()));
    }

    private static List<BlackListItem> updateTime(String symbol, List<BlackListItem> blackList) {
        blackList.forEach(item -> {
            if (item.getSymbol().equals(symbol)) {
                item.setTime(System.currentTimeMillis());
            }
        });
        return blackList;
    }

    private static boolean hasTokenBySymbolFromBlackList(String symbol, List<BlackListItem> blackList) {
        return blackList.stream().anyMatch(item -> item.getSymbol().equalsIgnoreCase(symbol));
    }

    public static boolean isTokenReady(String symbol1, String symbol2, Exchange exchange1, Exchange exchange2) {
        BlackListItem item1 = getTokenBySymbolFromBlackList(symbol1, exchange1.getBlackList());
        BlackListItem item2 = getTokenBySymbolFromBlackList(symbol2, exchange2.getBlackList());

        if (System.currentTimeMillis() - item1.getTime() > 3600000 && System.currentTimeMillis() - item2.getTime() > 3600000) {
            updateTime(symbol1, exchange1.getBlackList());
            updateTime(symbol2, exchange2.getBlackList());

            return true;
        }

        return false;
    }

    private static BlackListItem getTokenBySymbolFromBlackList(String symbol, List<BlackListItem> blackList) {
        return blackList.stream()
                .filter(item -> item.getSymbol().equals(symbol))
                .findFirst()
                .orElse(null);
    }


}