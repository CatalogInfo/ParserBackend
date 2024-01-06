package com.example.backend_parser.utils;

import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;

public class MessageUtils {

    public static String getFormattedMessage(
            Token token1,
            Token token2,
            Exchange exchange1,
            Exchange exchange2,
            double spread
    ) {
        return String.format(
                "%s\n<a href=\"%s%s%s\">%s</a>: buy %.2f\n<a href=\"%s%s%s\">%s</a>: sell %.2f\nspread: %.2f%%25",
                token1.getSymbol(),
                exchange1.getLink(), token1.getBase(), exchange1.getLinkSplitter(), exchange1.getName(), token1.getAsk(),
                exchange2.getLink(), token2.getBase(), exchange2.getLinkSplitter(), exchange2.getName(), token2.getBid(),
                spread
        );
    }
}