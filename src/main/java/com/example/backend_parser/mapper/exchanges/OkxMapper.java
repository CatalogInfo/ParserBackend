package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.backend_parser.utils.JsonUtils.*;

public class OkxMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONArray(obj, "data");
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        JSONArray data = getJSONArray(obj, "data");
        return getJSONObject(data.get(0));
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("instId", "baseCcy", "quoteCcy", "bids", "asks");
    }
}
