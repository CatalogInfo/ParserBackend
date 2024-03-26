package com.example.backend_parser.parser.mapper.exchanges;

import com.example.backend_parser.parser.mapper.base.KeysMapper;
import com.example.backend_parser.parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.getJSONArray;
import static com.example.backend_parser.utils.JsonUtils.getJSONObject;

public class XTcomMapper extends Mapper {
    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("symbol", "baseCurrency" ,"quoteCurrency");
    }

    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONObject jsonResult = JsonUtils.getJSONObject(obj, "result");
        return getJSONArray(jsonResult, "symbols");
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "result");
    }

    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray coins = new JSONArray(obj.getJSONArray("result"));

        for(int i = 0; i < coins.length(); i ++) {
            for(Token token : tokens) {
                JSONObject chainObj = coins.getJSONObject(i);
                String coin = chainObj.getString("currency");
                if(token.getBase().equalsIgnoreCase(coin)) {

                    JSONArray chainsDetails = chainObj.getJSONArray("supportChains");
                    for (int j = 0; j < chainsDetails.length(); j++) {
                        JSONObject chainDetail = chainsDetails.getJSONObject(j);
                        String chain = unifyChain(chainDetail.getString("chain"));

                        boolean depositEnable = chainDetail.getBoolean("depositEnabled");
                        boolean withdrawEnable = chainDetail.getBoolean("withdrawEnabled");
                        double fee = chainDetail.getDouble("withdrawFeeAmount");
                        Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }

    private String unifyChain(String chain) {
        switch(chain) {
            case "BNB Smart Chain":
                return "BSC";
            case "Polygon":
                return "MATIC";
            case "SOL-SOL":
                return "SOL";
            case "MA":
                return "MANTA";
            case "Ethereum":
                return "ETH";
            case "AVAX C-Chain":
                return "AVAXC";
            case "COREDAO":
                return "CORE";
            case "Bahamut":
                return "FTN";
        }

        return chain;
    }
}
