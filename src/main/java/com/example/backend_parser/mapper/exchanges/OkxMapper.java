package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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

    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray array = obj.getJSONArray("data");
        for(int i = 0; i < array.length(); i ++) {
            JSONObject ccyInfo = array.getJSONObject(i);
            String ccy = ccyInfo.getString("ccy");
            boolean canDeposit = ccyInfo.getBoolean("canDep");
            boolean canWithdraw = ccyInfo.getBoolean("canWd");
            String chain = parseChain(ccyInfo.getString("chain"));
            double fee = ccyInfo.getDouble("minFee");
            for(Token token : tokens) {
                if(token.getBase().equalsIgnoreCase(ccy)) {
                    Chain chain1 = new Chain(chain, canDeposit, canWithdraw, fee);
                    token.addChain(chain1);
                }
            }
        }
        System.out.println(tokens);
    }

    private String parseChain(String chain) {
        return chain.substring(chain.indexOf("-") + 1);
    }
}
