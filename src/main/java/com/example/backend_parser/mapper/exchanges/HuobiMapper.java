package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import org.json.JSONObject;

import static com.example.backend_parser.utils.JsonUtils.getJSONObject;
import static com.example.backend_parser.utils.JsonUtils.getValue;

public class HuobiMapper extends Mapper {
    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "tick");
    }

    @Override
    protected boolean quoteAssetAndCustomCheck(JSONObject symbolObject) {
        String state = String.valueOf(symbolObject.get("state"));
        String quoteAsset = getValue(symbolObject, getKeysMapper().getQuoteKey());

        return resolvedQuoteAssets.contains(quoteAsset.toUpperCase()) && state.equals("online");
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("bids", "asks");
    }
}