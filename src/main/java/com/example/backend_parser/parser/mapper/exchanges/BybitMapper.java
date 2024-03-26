package com.example.backend_parser.parser.mapper.exchanges;

import com.example.backend_parser.parser.mapper.base.KeysMapper;
import com.example.backend_parser.parser.mapper.base.Mapper;
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
    @Override
    public void convertChains(String response, List<Token> tokens) {
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

                        boolean depositEnable = defineIsIntBoolean(chainDetail.getString("chainDeposit"));
                        boolean withdrawEnable = defineIsIntBoolean(chainDetail.getString("chainWithdraw"));

                        double fee = defineIsStringDouble("withdrawFee", chainDetail);
                        double feePercent = defineIsStringDouble("withdrawPercentageFee", chainDetail);
                        Chain chain1 = new Chain(unifyChain(chain), depositEnable, withdrawEnable, fee, feePercent);
                        token.addChain(chain1);
                    }
                }
            }
        }
    }

    public double defineIsStringDouble(String name, JSONObject jsonObject) {
        String s = jsonObject.getString(name);
        if(s.equals("")) {
           return 0;
        }

        return Double.parseDouble(s);
    }

    public boolean defineIsIntBoolean(String number) {
        if(number.equals("1")) {
            return true;
        }
        return false;
    }

    public String unifyChain(String chain) {
        switch (chain) {
            case "ERC20":
                return "ETH";
            case  "BSC (BEP20)":
                return "BSC";
            case "Arbitrum One", "Arbitrum One (Bridged)":
                return "ARBITRUM";
            case "Polygon(bridged)", "Polygon", "Polygon ":
                return "POLYGON";
            case "Terra Classic":
                return "LUNC";
            case "Avalanche":
                return "AVAX";
            case "AVAX C-Chain", "AVAX-C":
                return "AVAXC";
            case "BNB (BEP2)":
                return "BNB";
            case "zkSync Era":
                return "ZKSYNCERA";
            case "TRC20":
                return "TRX";
            case "NEM":
                return "XEM";
            case "Base Mainnet":
                return "BASE";
            case "Stellar Lumens":
                return "XLM";
            case "Dogecoin":
                return "DOGE";
            case "Ethereum Classic":
                return "ETC";
            case "Bitcoin Cash":
                return "BCH";
            case "Solana":
                return "SOL";
            case "APTOS":
                return "APT";
            case "Celestia":
                return "TIA";
            case "Viction":
                return "VIC";
            case "BRC20 - unisat":
                return "ORDIBTC";
            case "Filecoin":
                return "FIL";
            case "Terra":
                return "LUNA";
            case "Mantle Network":
                return "MNT";
            case "Chiliz Legacy Chain":
                return "CHZ";
            case "Kaspa":
                return "KAS";
            case "Arbitrum Nova":
                return "ARBNOVA";
            case "Neon EVM":
                return "NEON";
            case "Luniverse":
                return "LUK";
            case "OMEGA":
                return "OMG";
            case "Caduceus":
                return "CMP";
            case "STEP":
                return "FITFI";
        }
        return chain;
    }


}