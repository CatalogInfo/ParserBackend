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
                        String chain = unifyChain(chainDetail.getString("chain"));
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

    private String unifyChain(String chain) {
        if(chain.startsWith("erc20")) {
            return "ETH";
        } else if (chain.startsWith("bep20")) {
            return "BSC";
        } else if (chain.startsWith("arbi")) {
            return "ARBITRUM";
        } else if (chain.startsWith("base")) {
            return "BASE";
        } else if (chain.startsWith("opt")) {
            return "OPTIMISM";
        } else if (chain.startsWith("hrc20")) {
            return "HRC20";
        } else if (chain.startsWith("trc20")) {
            return "TRX";
        } else if (chain.startsWith("btt2")) {
            return "BTT";
        }

        switch(chain) {
            case "ordi":
                return "ORDIBTC";
            case "poly1":
                return "POLYX";
            case "elf1":
                return "ELF";
            case "xec1":
                return "XEC";
            case "wax1":
                return "WAX";
            case "luna1":
                return "LUNA";
            case "sxp1":
                return "SXP";
            case "lsk1":
                return "LSK";
            case "eur":
                return "FIAT_MONEY";
            case "ctxc1":
                return "CTXC";
            case "kava10":
                return "KAVA";
            case "band2":
                return "BAND";
            case "one1":
                return "ONE";
            case "atom1":
                return "ATOM";
            case "enj1":
                return "ENJ";
            case "theta1":
                return "THETA";
            case "xmr1":
                return "XMR";
            case "zil1":
                return "ZIL";
            case "cchainavax":
                return "AVAXC";
            case "nuls1":
                return "NULS";
            case "icx1":
                return "ICX";
            case "ont2":
                return "ONT";
            case "iota1":
                return "IOTA";
            case "trx1":
                return "TRX";
            case "neo1":
                return "NEO";
            case "bnb1":
                return "BNB";
            case "eos1":
                return "EOS";
            case "arb":
                return "ARBITRUM";
            case "mnt1":
                return "MNT";
            case "tenet1":
                return "TENET";
            case "wemix1":
                return "WEMIX";
            case "wicc1":
                return "WICC";
            case "smt2":
                return "SMT";
            case "dbc1":
                return "DBC";

        }
        return chain;
    }
}