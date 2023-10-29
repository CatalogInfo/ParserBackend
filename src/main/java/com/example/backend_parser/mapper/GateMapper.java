package com.example.backend_parser.mapper;

import com.example.backend_parser.models.BaseQuote;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GateMapper extends Mapper {

    @Override
    public List<BaseQuote> mapBaseQuote(String response) {
        JSONArray symbols = new JSONArray(response);

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < symbols.length(); i ++) {
            JSONObject symbolObject = new JSONObject(String.valueOf(symbols.get(i)));

            String symbol = String.valueOf(symbolObject.get("id"));
            String baseAsset = String.valueOf(symbolObject.get("base"));
            String quoteAsset = String.valueOf(symbolObject.get("quote"));

            baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
        }

        return baseQuoteList;
    }
}
