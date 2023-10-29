package com.example.backend_parser.calculations;

import com.example.backend_parser.models.BidAsk;
import com.example.backend_parser.models.BidsAsks;
import com.example.backend_parser.models.Order;

import java.util.ArrayList;

public class PriceCalculator {
    public static BidAsk calculatePriceForLiquidity(int liquidity, BidsAsks bidsAsks) {
        ArrayList<Order> bids = bidsAsks.getBids();
        ArrayList<Order> asks = bidsAsks.getAsks();

        if (bids.isEmpty()) {
            return new BidAsk();
        }

        if (asks.isEmpty()) {
            return new BidAsk();
        }

        double bid = findPrice(bids, liquidity);
        double ask = findPrice(asks, liquidity);

        return new BidAsk(bid, ask);
    }
    private static double findPrice(ArrayList<Order> orders, int liquidity) {
        double tokensAmount = 0;
        double amountInUsdt = 0;

        for(Order order : orders) {
            if (amountInUsdt > liquidity) {
                break;
            }
            double price = order.getPrice();
            double amount = order.getAmount();

            tokensAmount = amount + tokensAmount;
            amountInUsdt = amountInUsdt + (amount * price);
        }

        return calculateFinalPrice(tokensAmount, amountInUsdt);
    }
    private static double calculateFinalPrice(double tokensAmount, double amountInUsdt) {
        return amountInUsdt/tokensAmount;
    }
}
