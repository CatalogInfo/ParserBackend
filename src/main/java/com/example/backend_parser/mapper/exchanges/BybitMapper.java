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

public class BybitMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        JSONObject result = getJSONObject(obj, "result");
        return getJSONArray(result, "list");
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONObject(obj, "result");
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("symbol", "baseCoin", "quoteCoin", "b", "a");
    }
    public void mapChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        JSONObject result = obj.getJSONObject("result");
        JSONArray rows = result.getJSONArray("rows");

        for(int i = 0; i < rows.length(); i ++) {
            for(Token token : tokens) {

                JSONObject chainObj = rows.getJSONObject(i);
                String coin = chainObj.getString("coin");
                if(token.getBase().equalsIgnoreCase(coin)) {

                    JSONArray chainsDetails = chainObj.getJSONArray("chains");
                    for (int j = 0; j < chainsDetails.length(); j++) {
                        JSONObject chainDetail = chainsDetails.getJSONObject(j);
                        String chain = chainDetail.getString("chainType");

                        boolean depositEnable = defineIsIntBoolean(chainDetail.getInt("chainDeposit"));
                        boolean withdrawEnable = defineIsIntBoolean(chainDetail.getInt("chainWithdraw"));

                        double fee = chainDetail.getDouble("withdrawFee");
                        double feePercent = chainDetail.getDouble("withdrawPercentageFee");
                        Chain chain1 = new Chain(chain, depositEnable, withdrawEnable, fee, feePercent);
                        System.out.println(chain1);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }

    public boolean defineIsIntBoolean(int number) {
        if(number == 1) {
            return true;
        }
        return false;
    }

}