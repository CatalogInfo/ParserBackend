package com.example.backend_parser.parser.mapper.exchanges;

import com.example.backend_parser.parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BinanceMapper extends Mapper {
    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONArray array = JsonUtils.getJSONArray(response);
        for(Token token : tokens) {
            for(int i = 0; i < array.length(); i ++) {
                JSONObject coinObject = array.getJSONObject(i);
                String name = coinObject.getString("coin");

                if(token.getBase().equalsIgnoreCase(name)) {
                    JSONArray networkList = coinObject.getJSONArray("networkList");

                    for(int j = 0; j < networkList.length(); j ++) {
                        JSONObject networkObject = networkList.getJSONObject(j);
                        String chain = unifyChain(networkObject.getString("network"));

                        boolean depositEnable = networkObject.getBoolean("depositEnable");
                        boolean withdrawEnable = networkObject.getBoolean("withdrawEnable");
                        double fee = networkObject.getDouble("withdrawFee");
                        Chain chainObj = new Chain(chain, depositEnable, withdrawEnable, fee);
                        token.addChain(chainObj);
                    }
                }
            }
        }
    }

    public String unifyChain(String chain) {

        switch (chain) {
            case "AVAX_C":
                return "AVAXC";
            case  "WAXP":
                return "WAX";
        }
        return chain;
    }
}
