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

public class HuobiMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONArray(obj, "data");
    }

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
        return new KeysMapper("sc", "bcdn" ,"qcdn", "bids", "asks");
    }

    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray coins = new JSONArray(obj.getJSONArray("data"));

        for(int i = 0; i < coins.length(); i ++) {
            for(Token token : tokens) {
                JSONObject chainObj = coins.getJSONObject(i);
                String coin = chainObj.getString("currency");
                if(token.getBase().equalsIgnoreCase(coin)) {
                    JSONArray chainsDetails = chainObj.getJSONArray("chains");
                    for (int j = 0; j < chainsDetails.length(); j++) {
                        JSONObject chainDetail = chainsDetails.getJSONObject(j);
                        String chain = chainDetail.getString("chain");
                        boolean depositEnable = false;
                        boolean withdrawEnable = false;
                        String dep = chainDetail.getString("depositStatus");
                        if (dep.equals("allowed")){
                            depositEnable = true;
                        }
                        String withdraw = chainDetail.getString("withdrawStatus");
                        if (withdraw.equals("allowed")){
                            withdrawEnable = true;
                        }
                        String feeType = chainDetail.getString("withdrawFeeType");
                        if(feeType.equals("fixed")){
                            double fee = chainDetail.getDouble("transactFeeWithdraw");
                            Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee);
                            token.addChain(chain1);
                        }else {
                            double fee = 0;
                            double feePercent = chainDetail.getDouble("transactFeeRateWithdraw") * 100;
                            Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee, feePercent);
                            token.addChain(chain1);
                        }
                    }
                }
            }
        }
    }
}