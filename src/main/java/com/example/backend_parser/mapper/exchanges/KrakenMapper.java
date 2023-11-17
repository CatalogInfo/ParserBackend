package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.BaseQuote;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.*;
import static com.example.backend_parser.utils.JsonUtils.getValue;

public class KrakenMapper extends Mapper {
    protected JSONObject getResult(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "result");
    }

    @Override
    public List<BaseQuote> convertBaseQuote(String response) {
        JSONObject symbols = getResult(response);

        List<BaseQuote> baseQuoteList = new ArrayList<>();

        Iterator<String> keys = symbols.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject symbolObject = getJSONObject(symbols, key);

            String symbol = getValue(symbolObject, getKeysMapper().getSymbolKey());
            String baseAsset = getValue(symbolObject, getKeysMapper().getBaseKey());
            String quoteAsset = getValue(symbolObject, getKeysMapper().getQuoteKey());

            if(quoteAssetAndCustomCheck(symbolObject)) {
                baseQuoteList.add(new BaseQuote(symbol, baseAsset, quoteAsset));
            }
        }

        return baseQuoteList;
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        JSONObject data = getJSONObject(obj, "result");
        return getJSONObject(data, data.keys().next());
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("altname", "base", "quote", "bids", "asks");
    }
}
