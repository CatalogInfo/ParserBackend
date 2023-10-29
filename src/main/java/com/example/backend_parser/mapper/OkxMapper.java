package com.example.backend_parser.mapper;

import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.models.BidsAsks;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OkxMapper extends Mapper {
    @Override
    protected BidsAsks mapOrderBook(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray data = new JSONArray(String.valueOf(obj.get("data")));
        JSONObject dataObject = new JSONObject(String.valueOf(data.get(0)));

        JSONArray bids = new JSONArray(String.valueOf(dataObject.get("bids")));
        JSONArray asks = new JSONArray(String.valueOf(dataObject.get("asks")));

        return convertJSONArrayToBidsAsks(bids, asks);
    }

    @Override
    public List<BaseQuote> mapBaseQuote(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray symbols = new JSONArray(String.valueOf(obj.get("data")));

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < symbols.length(); i ++) {
            JSONObject symbolObject = new JSONObject(String.valueOf(symbols.get(i)));

            String symbol = String.valueOf(symbolObject.get("instId"));
            String baseAsset = String.valueOf(symbolObject.get("baseCcy"));
            String quoteAsset = String.valueOf(symbolObject.get("quoteCcy"));

            baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
        }

        return baseQuoteList;
    }
}
