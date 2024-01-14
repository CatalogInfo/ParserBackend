package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BinanceMapper extends Mapper {
    public void mapChains(String response, List<Token> tokens) {
        JSONArray array = JsonUtils.getJSONArray(response);
        for(Token token : tokens) {
            for(int i = 0; i < array.length(); i ++) {
                JSONObject coinObject = array.getJSONObject(i);
                String name = coinObject.getString("coin");

                if(token.getBase().equalsIgnoreCase(name)) {
                    JSONArray networkList = coinObject.getJSONArray("networkList");

                    for(int j = 0; j < networkList.length(); j ++) {
                        JSONObject networkObject = networkList.getJSONObject(j);
                        String chain = networkObject.getString("network");
                        System.out.println(networkObject);

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
}
