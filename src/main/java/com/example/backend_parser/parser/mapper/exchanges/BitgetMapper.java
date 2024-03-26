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

public class BitgetMapper extends Mapper {

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("symbol", "baseCoin", "quoteCoin");
    }
    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "data");
    }

    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONArray(obj, "data");
    }

    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONArray coins = new JSONArray(obj.getJSONArray("data"));

        for(int i = 0; i < coins.length(); i ++) {
            for(Token token : tokens) {
                JSONObject chainObj = coins.getJSONObject(i);
                String coin = chainObj.getString("coin");
                if(token.getBase().equalsIgnoreCase(coin)) {

                    JSONArray chainsDetails = chainObj.getJSONArray("chains");
                    for (int j = 0; j < chainsDetails.length(); j++) {
                        JSONObject chainDetail = chainsDetails.getJSONObject(j);
                        String chain = unifyChain(chainDetail.getString("chain"));

                        boolean depositEnable = chainDetail.getBoolean("rechargeable");
                        boolean withdrawEnable = chainDetail.getBoolean("withdrawable");
                        double fee = chainDetail.getDouble("withdrawFee");
                        double feePercent = convertDoubleToPercent(chainDetail.getDouble("extraWithdrawFee"));
                        Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee, feePercent);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }

    private double convertDoubleToPercent(double fee) {
        return fee * 100;
    }

    private String unifyChain(String chain) {
        switch(chain) {
            case "ERC20":
                return "ETH";
            case "BEP20":
                return "BSC";
            case "C-Chain":
                return "AVAXC";
            case "Polygon", "POLYGON":
                return "MATIC";
            case "BRC20":
                return "ORDIBTC";
            case "zkSync":
                return "ZKSYNCERA";
            case "COREDAO":
                return "CORE";
            case "Bahamut":
                return "FTN";
        }

        return chain;
    }

}
