package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BitrueMapper extends Mapper {
    public void mapChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray coins = new JSONArray(obj.getJSONArray("coins"));

        for(int i = 0; i < coins.length(); i ++) {
            for(Token token : tokens) {
                JSONObject chainObj = coins.getJSONObject(i);
                String coin = chainObj.getString("coin");
                if(token.getBase().equalsIgnoreCase(coin)) {

                    JSONArray chainsDetails = chainObj.getJSONArray("chainDetail");
                    for (int j = 0; j < chainsDetails.length(); j++) {
                        JSONObject chainDetail = chainsDetails.getJSONObject(j);
                        String chain = chainDetail.getString("chain");

                        boolean depositEnable = chainDetail.getBoolean("depositEnable");
                        boolean withdrawEnable = chainDetail.getBoolean("withdrawEnable");
                        double fee = chainDetail.getDouble("withdrawFee");
                        Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }
}