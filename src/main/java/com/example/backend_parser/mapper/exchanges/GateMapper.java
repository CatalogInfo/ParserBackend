package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.*;

public class GateMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        return getJSONArray(response);
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("id", "base", "quote");
    }

    public void mapChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        Iterator keys = obj.keys();

        while(keys.hasNext()) {
            JSONObject coin = obj.getJSONObject(String.valueOf(keys.next()));
            Iterator keysInside = coin.keys();
            while(keysInside.hasNext()) {
                String key = String.valueOf(keysInside.next());
                if(key.startsWith("withdraw_fix_on_chain_")) {
                    String chain = key.substring(23);
                    String fee = coin.getString(key);
                }
            }
        }
    }
}
