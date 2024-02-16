package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.*;


public class LBankMapper extends Mapper {
    @Override
    public List<Token> convertBaseQuote(String response) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray symbols = JsonUtils.getJSONArray(obj, "data");
        List<Token> baseQuoteList = new ArrayList<>();

        for (int i = 0; i < symbols.length(); i ++) {
            String symbol = symbols.getString(i);
            String baseAsset = symbol.substring(0, symbol.indexOf("_"));
            String quoteAsset = symbol.substring(symbol.indexOf("_") + 1);

            if(quoteAssetAndCustomCheck(quoteAsset)) {
                baseQuoteList.add(new Token(symbol, baseAsset, quoteAsset));
            }
        }

        return baseQuoteList;
    }
    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "data");
    }

}
