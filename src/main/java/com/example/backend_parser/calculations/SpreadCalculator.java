package com.example.backend_parser.calculations;

import com.example.backend_parser.models.Token;

public class SpreadCalculator {
    public static double calculateSpread(Token token1, Token token2) {
        double bid1 = token1.getBid();
        double bid2 = token2.getBid();
        double ask1 = token1.getAsk();
        double ask2 = token2.getAsk();


        if (bid1 > ask2) {
            return percentBetweenPrices(bid1, ask2);
        }
        if (bid2 > ask1) {
            return percentBetweenPrices(bid2, ask1);
        }

        return 0;
    }

    public static boolean isNotNull(double bid, double ask) {
        return bid != 0.0 && ask != 0.0;
    }

    public static double percentBetweenPrices(
            double firstTokenPrice,
            double secondTokenPrice
    ) {
        return (
                (Math.abs(firstTokenPrice - secondTokenPrice) / secondTokenPrice) * 100
        );
    }
}
