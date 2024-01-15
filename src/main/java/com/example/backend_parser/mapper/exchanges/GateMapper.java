package com.example.backend_parser.mapper.exchanges;

import com.example.backend_parser.mapper.base.KeysMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.RequestMaker;
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
        setDepositWithdraw(tokens);
        JSONObject obj = JsonUtils.getJSONObject(response);
        Iterator keys = obj.keys();

        while(keys.hasNext()) {
            String symbol = String.valueOf(keys.next());

            for(Token token : tokens) {
                JSONObject coin = obj.getJSONObject(symbol);
                Iterator keysInside = coin.keys();
                if(token.getBase().equalsIgnoreCase(symbol)) {

                    while (keysInside.hasNext()) {
                        String key = String.valueOf(keysInside.next());
                        String withdrawPercent = coin.getString("withdraw_percent");
                        double feePercent = Double.parseDouble(withdrawPercent.substring(0, withdrawPercent.length() - 1));
                        if (key.startsWith("withdraw_fix_on_chain_")) {

                            String chainName = key.substring(22);
                            double fee = coin.getDouble(key);
                            for(Chain chain : token.getChains()) {
                                if(chain.getName().equalsIgnoreCase(chainName)) {
                                    chain.setName(chainName);
                                    chain.setFee(fee);
                                    chain.setFeePercent(feePercent);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setDepositWithdraw(List<Token> tokens) {
        String url = "https://api.gateio.ws/api/v4/spot/currencies";
        String response = RequestMaker.getRequest(url);
        JSONArray arrayInitial = JsonUtils.getJSONArray(response);
        for (Token token : tokens) {
            for (int i = 0; i < arrayInitial.length(); i++) {
                JSONObject obj = arrayInitial.getJSONObject(i);
                String currency = obj.getString("currency");
                boolean withdraw_disabled = obj.getBoolean("withdraw_disabled");
                boolean deposit_disabled = obj.getBoolean("deposit_disabled");
                deposit_disabled = !deposit_disabled;
                withdraw_disabled = !withdraw_disabled;
                String chain = obj.getString("chain");

                if (currency.contains("_")) {
                    currency = currency.substring(0, currency.indexOf("_"));
                }

                if (currency.equalsIgnoreCase(token.getBase())) {
                    Chain chain1 = new Chain(chain, deposit_disabled, withdraw_disabled);
                    token.addChain(chain1);
                }
            }
        }
    }
}
