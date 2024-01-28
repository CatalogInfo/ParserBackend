package com.example.backend_parser.mapper.base;

import com.example.backend_parser.models.BidsAsks;
import com.example.backend_parser.models.Order;
import org.json.JSONArray;
import org.json.JSONObject;

public class Converter extends Obtainer {
    protected BidsAsks convertOrderBook(String response) {
        JSONObject obj = new JSONObject();
        try {
            obj = getOrderBookData(response);
        }catch (Exception e) {
            return new BidsAsks();
        }

        if (!obj.has(getKeysMapper().getBidsKey()) || String.valueOf(obj.get(getKeysMapper().getBidsKey())).equals("null") || obj.equals(new JSONObject())) {
            return new BidsAsks();
        }

        JSONArray bids = new JSONArray(String.valueOf(obj.get(getKeysMapper().getBidsKey())));
        JSONArray asks = new JSONArray(String.valueOf(obj.get(getKeysMapper().getAsksKey())));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

    protected BidsAsks convertJSONArrayToBidsAsks(JSONArray bids, JSONArray asks) {
        BidsAsks bidsAsks = new BidsAsks();

        for(int i = 0; i < bids.length(); i ++) {
            JSONArray orderBids = new JSONArray(String.valueOf(bids.get(i)));
            bidsAsks.addOrderToBids(getOrder(orderBids));
        }

        for(int i = 0; i < asks.length(); i ++) {
            JSONArray orderAsks = new JSONArray(String.valueOf(asks.get(i)));
            bidsAsks.addOrderToAsks(getOrder(orderAsks));
        }

        return bidsAsks;
    }

    private Order getOrder(JSONArray array) {
        double price = Double.parseDouble(String.valueOf(array.get(0)));
        double amount = Double.parseDouble(String.valueOf(array.get(1)));

        return new Order(price, amount);
    }

}
