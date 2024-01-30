package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MexcMapper extends Mapper {
    @Override
    public void convertChains(String response, List<Token> tokens) {
//        System.out.println(response);

        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray array = obj.getJSONArray("data");
        for(int i = 0; i < array.length(); i ++) {
            JSONObject ccyInfo = array.getJSONObject(i);

            String ccy = ccyInfo.getString("currency");
            for(Token token : tokens) {
                JSONArray coins = ccyInfo.getJSONArray("coins");
                for (int j = 0; j < coins.length(); j++) {
                    try {
                        String canDeposit = ccyInfo.getString("is_deposit_enabled");
                        String canWithdraw = ccyInfo.getString("is_withdraw_enabled");
                        String chain = ccyInfo.getString("chain");
                        double fee = ccyInfo.getDouble("fee");
                        if (token.getBase().equalsIgnoreCase(ccy)) {
                            Chain chain1 = new Chain(chain, Boolean.getBoolean(canDeposit), Boolean.getBoolean(canWithdraw), fee);
                            token.addChain(chain1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
