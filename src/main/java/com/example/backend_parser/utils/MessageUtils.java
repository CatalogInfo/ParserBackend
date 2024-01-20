package com.example.backend_parser.utils;

import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;

public class MessageUtils {

    public static String getFormattedMessage(Token token1, Token token2, Exchange exchange1, Exchange exchange2, double spread, String chain) {
        return token1.getBase() + "\n" +
                        "<a href=\"" + exchange1.getLink() + token1.getBase() + exchange1.getLinkSplitter() + token1.getQuote() + exchange1.getEndOfLink() + "\"> " +
                        exchange1.getName() + " </a>: buy " + token1.getAsk() + "\n" +
                        "<a href=\"" + exchange2.getLink() + token2.getBase() + exchange2.getLinkSplitter() + token2.getQuote() + exchange2.getEndOfLink() + "\"> " +
                        exchange2.getName() + " </a>: sell " + token2.getBid() + "\n" +
                        "spread: " + String.format("%.2f%%25", spread) + "\n" +
                        "chain: " + chain;
    }
}