package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MexcMapper extends Mapper {
    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        System.out.println(response);
        JSONArray array = obj.getJSONArray("data");
        for(int i = 0; i < array.length(); i ++) {
            JSONObject ccyInfo = array.getJSONObject(i);
            String ccy = ccyInfo.getString("currency");
            for(Token token : tokens) {
                JSONArray coins = ccyInfo.getJSONArray("coins");
                for (int j = 0; j < coins.length(); j++) {
                    System.out.println(coins.get(i));
                    boolean canDeposit = ccyInfo.getBoolean("is_deposit_enabled");
                    boolean canWithdraw = ccyInfo.getBoolean("is_withdraw_enabled");
                    String chain = ccyInfo.getString("chain");
                    double fee = ccyInfo.getDouble("fee");
                    if(token.getBase().equalsIgnoreCase(ccy)) {
                        Chain chain1 = new Chain(chain, canDeposit, canWithdraw, fee);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }
}
