package com.example.backend_parser.mapper;

import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.BidsAsks;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HuobiMapper extends Mapper {
    @Override
    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONObject tick = new JSONObject(String.valueOf(obj.get("tick")));

        JSONArray bids = new JSONArray(String.valueOf(tick.get("bids")));
        JSONArray asks = new JSONArray(String.valueOf(tick.get("asks")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

    @Override
    public List<BaseQuote> mapBaseQuote(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray data = new JSONArray(String.valueOf(obj.get("data")));

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < data.length(); i ++) {
            JSONObject symbolObject = new JSONObject(String.valueOf(data.get(i)));

            String symbol = String.valueOf(symbolObject.get("dn"));
            String baseAsset = String.valueOf(symbolObject.get("bcdn"));
            String quoteAsset = String.valueOf(symbolObject.get("qcdn"));

            baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
        }

        return baseQuoteList;
    }

}