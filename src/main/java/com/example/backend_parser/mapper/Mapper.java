package com.example.backend_parser.mapper;

import com.example.backend_parser.calculations.PriceCalculator;
import com.example.backend_parser.models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    List<String> resolvedQuoteAssets = new ArrayList<>();

    public Token mapResponseToToken(String response, String symbol) {
        BidsAsks orderBook = mapOrderBook(response);
        BidAsk bidAsk = PriceCalculator.calculatePriceForLiquidity(2000, orderBook);

        return new Token(symbol, bidAsk.getBid(), bidAsk.getAsk());
    }

    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray bids = new JSONArray(String.valueOf(obj.get("bids")));
        JSONArray asks = new JSONArray(String.valueOf(obj.get("asks")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

    public List<BaseQuote> mapBaseQuote(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray symbols = new JSONArray(String.valueOf(obj.get("symbols")));

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < symbols.length(); i ++) {
            JSONObject symbolObject = new JSONObject(String.valueOf(symbols.get(i)));

            String symbol = String.valueOf(symbolObject.get("symbol"));
            String baseAsset = String.valueOf(symbolObject.get("baseAsset"));
            String quoteAsset = String.valueOf(symbolObject.get("quoteAsset"));

            if(resolvedQuoteAssets.contains(quoteAsset.toUpperCase())) {
                baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
            }
        }

        return baseQuoteList;
    }
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
