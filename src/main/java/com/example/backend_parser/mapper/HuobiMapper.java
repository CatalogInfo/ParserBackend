package com.example.backend_parser.mapper;

import com.example.backend_parser.models.BidsAsks;
import org.json.JSONArray;
import org.json.JSONObject;

public class HuobiMapper extends Mapper {
    @Override
    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONObject tick = new JSONObject(String.valueOf(obj.get("tick")));

        JSONArray bids = new JSONArray(String.valueOf(tick.get("bids")));
        JSONArray asks = new JSONArray(String.valueOf(tick.get("asks")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

}