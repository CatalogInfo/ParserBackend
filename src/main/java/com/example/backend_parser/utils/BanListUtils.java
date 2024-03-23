package com.example.backend_parser.utils;

import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;

import java.util.List;

public class BanListUtils {
    public static boolean tokenInBanList(Token token, Exchange exchange) {
        return hasTokenBySymbolFromBanList(token.getBase() + token.getQuote(), exchange.getBanList());
    }

    private static boolean hasTokenBySymbolFromBanList(String symbol, List<BanToken> banList) {
        return banList.stream()
                .anyMatch(item -> item.getToken().equalsIgnoreCase(symbol));
    }
}
