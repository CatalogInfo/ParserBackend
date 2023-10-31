package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.backend_parser.utils.JsonUtils.*;

public class BybitMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        JSONObject result = getJSONObject(obj, "result");
        return getJSONArray(result, "list");
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "result");
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("symbol", "baseCoin", "quoteCoin", "b", "a");
    }
}