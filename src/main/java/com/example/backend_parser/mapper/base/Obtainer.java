package com.example.backend_parser.mapper.base;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.backend_parser.utils.JsonUtils.getJSONArray;
import static com.example.backend_parser.utils.JsonUtils.getJSONObject;

public class Obtainer {
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONArray(obj, "symbols");
    }

    protected JSONObject getOrderBookData(String response) {

        return getJSONObject(response);
    }

    protected KeysMapper getKeysMapper() {
        return new KeysMapper("symbol", "baseAsset", "quoteAsset", "bids", "asks");
    }

}
