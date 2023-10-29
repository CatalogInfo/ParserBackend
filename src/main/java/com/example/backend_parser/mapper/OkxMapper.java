package com.example.backend_parser.mapper;

import com.example.backend_parser.calculations.PriceCalculator;
import com.example.backend_parser.models.BidAsk;
import com.example.backend_parser.models.BidsAsks;
import com.example.backend_parser.models.Order;
import com.example.backend_parser.models.Token;
import org.json.JSONArray;
import org.json.JSONObject;

public class OkxMapper extends Mapper {
    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray data = new JSONArray(String.valueOf(obj.get("data")));
        JSONObject dataObject = new JSONObject(String.valueOf(data.get(0)));

        JSONArray bids = new JSONArray(String.valueOf(dataObject.get("bids")));
        JSONArray asks = new JSONArray(String.valueOf(dataObject.get("asks")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }
}
