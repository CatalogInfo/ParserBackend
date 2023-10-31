package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import org.json.JSONArray;

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
}
