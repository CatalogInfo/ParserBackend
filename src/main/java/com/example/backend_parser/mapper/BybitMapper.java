package com.example.backend_parser.mapper;

import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.BidsAsks;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BybitMapper extends Mapper {
    @Override
    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONObject tick = new JSONObject(String.valueOf(obj.get("result")));

        JSONArray bids = new JSONArray(String.valueOf(tick.get("b")));
        JSONArray asks = new JSONArray(String.valueOf(tick.get("a")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

    @Override
    public List<BaseQuote> mapBaseQuote(String response) {
        JSONObject obj = new JSONObject(response);
        JSONObject result = new JSONObject(String.valueOf(obj.get("result")));
        JSONArray symbols = new JSONArray(String.valueOf(result.get("list")));

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < symbols.length(); i ++) {
            JSONObject symbolObject = new JSONObject(String.valueOf(symbols.get(i)));

            String symbol = String.valueOf(symbolObject.get("symbol"));
            String baseAsset = String.valueOf(symbolObject.get("baseCoin"));
            String quoteAsset = String.valueOf(symbolObject.get("quoteCoin"));

            if(resolvedQuoteAssets.contains(quoteAsset.toUpperCase())) {
                baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
            }
        }

        return baseQuoteList;
    }
}