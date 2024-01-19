package com.example.backend_parser.utils;

import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;

public class MessageUtils {

    public static String getFormattedMessage(Token token1, Token token2, Exchange exchange1, Exchange exchange2, double spread, String chain) {
        return String.format(
                "%s\n<a href=\"%s\"> %s </a>: buy %s\n<a href=\"%s\"> %s </a>: sell %s\nspread: %.2f%%25\nchain: %s",
                token1.getSymbol(),
                exchange1.getLink() + token1.getBase() + exchange1.getLinkSplitter() + token1.getQuote() + exchange1.getEndOfLink(),
                exchange1.getName(),
                token1.getAsk(),
                exchange2.getLink() + token2.getBase() + exchange2.getLinkSplitter() + token2.getQuote() + exchange2.getEndOfLink(),
                exchange2.getName(),
                token2.getBid(),
                spread,
                chain
        );
    }
}