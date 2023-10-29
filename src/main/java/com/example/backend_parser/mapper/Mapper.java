package com.example.backend_parser.mapper;

import com.example.backend_parser.calculations.PriceCalculator;
import com.example.backend_parser.models.BidAsk;
import com.example.backend_parser.models.BidsAsks;
import com.example.backend_parser.models.Order;
import com.example.backend_parser.models.Token;
import org.json.JSONArray;

public abstract class Mapper {
    public Token mapResponseToToken(String response, String symbol) {
        BidsAsks orderBook = mapOrderBook(response);
        BidAsk bidAsk = PriceCalculator.calculatePriceForLiquidity(2000, orderBook);

        return new Token(symbol, bidAsk.getBid(), bidAsk.getAsk());
    }

    protected abstract BidsAsks mapOrderBook(String response);

    protected BidsAsks convertJSONArrayToBidsAsks(JSONArray bids, JSONArray asks) {
        BidsAsks bidsAsks = new BidsAsks();

        for(int i = 0; i < bids.length(); i ++) {
            JSONArray orderJsonArray = new JSONArray(String.valueOf(bids.get(i)));
            bidsAsks.addOrderToBids(getOrder(orderJsonArray));
        }

        for(int i = 0; i < asks.length(); i ++) {
            JSONArray orderJsonArray = new JSONArray(String.valueOf(asks.get(i)));
            bidsAsks.addOrderToAsks(getOrder(orderJsonArray));
        }

        return bidsAsks;
    }

    protected Order getOrder(JSONArray array) {
        double price = Double.parseDouble(String.valueOf(array.get(0)));
        double amount = Double.parseDouble(String.valueOf(array.get(1)));
        return new Order(price, amount);
    }
}
