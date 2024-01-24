package com.example.backend_parser.calculations;

import com.example.backend_parser.models.BidAsk;
import com.example.backend_parser.models.BidsAsks;
import com.example.backend_parser.models.Order;

import java.util.ArrayList;

public class PriceCalculator {
    public static BidAsk calculatePriceForLiquidity(int liquidity, BidsAsks bidsAsks) {
        ArrayList<Order> bids = bidsAsks.getBids();
        ArrayList<Order> asks = bidsAsks.getAsks();

        double bid = 0;
        double ask = 0;

        if (!bids.isEmpty()) {
            bid = findPrice(bids, liquidity);
        }

        if (!asks.isEmpty()) {
            ask = findPrice(asks, liquidity); new BidAsk();
        }

        return new BidAsk(bid, ask);
    }
    private static double findPrice(ArrayList<Order> orders, int liquidity) {
        double tokensAmount = 0;
        double amountInUsdt = 0;

        for(Order order : orders) {
            if (amountInUsdt > liquidity) {
                return calculateFinalPrice(tokensAmount, amountInUsdt);
            }
            double price = order.getPrice();
            double amount = order.getAmount();

            tokensAmount = amount + tokensAmount;
            amountInUsdt = amountInUsdt + (amount * price);
        }

        return 0;
    }
    private static double calculateFinalPrice(double tokensAmount, double amountInUsdt) {
        return amountInUsdt/tokensAmount;
    }
}
