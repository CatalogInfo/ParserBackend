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

public class OkxMapper extends Mapper {
    @Override
    protected JSONArray getSymbols(String response) {
        JSONObject obj = getJSONObject(response);
        return getJSONArray(obj, "data");
    }

    @Override
    protected JSONObject getOrderBookData(String response) {
        JSONObject obj = getJSONObject(response);
        JSONArray data = getJSONArray(obj, "data");
        return getJSONObject(data.get(0));
    }

    @Override
    public KeysMapper getKeysMapper() {
        return new KeysMapper("instId", "baseCcy", "quoteCcy", "bids", "asks");
    }

    @Override
    public void convertChains(String response, List<Token> tokens) {
        JSONObject obj = JsonUtils.getJSONObject(response);
        if(!obj.has("data")) {
            return;
        }
        JSONArray array = obj.getJSONArray("data");
        for(int i = 0; i < array.length(); i ++) {
            JSONObject ccyInfo = array.getJSONObject(i);
            String ccy = ccyInfo.getString("ccy");
            boolean canDeposit = ccyInfo.getBoolean("canDep");
            boolean canWithdraw = ccyInfo.getBoolean("canWd");
            String chain = unifyChain(parseChain(ccyInfo.getString("chain")));
            double fee = ccyInfo.getDouble("minFee");
            for(Token token : tokens) {
                if(token.getBase().equalsIgnoreCase(ccy)) {
                    Chain chain1 = new Chain(chain, canDeposit, canWithdraw, fee);
                    token.addChain(chain1);
                }
            }
        }
    }

    private String parseChain(String chain) {
        return chain.substring(chain.indexOf("-") + 1);
    }

    public String unifyChain(String chain) {
        switch (chain) {
            case "ERC20":
                return "ETH";
            case  "BSC (BEP20)":
                return "BSC";
            case "Bitcoin":
                return "BTC";
            case "Arbitrum One":
                return "ARBITRUM";
            case "zkSync Era":
                return "ZKSYNCERA";
            case "Ripple":
                return "XRP";
            case "Solana":
                return "SOL";
            case "Dogecoin":
                return "DOGE";
            case "Acala":
                return "ACA";
            case "Endurance Smart Chain":
                return "ENDURANCE";
            case "Cardano":
                return "ADA";
            case "Algorand":
                return "ALGO";
            case "Aptos":
                return "APT";
            case "Arweave":
                return "AR";
            case "Astar":
                return "ASTR";
            case "Cosmos":
                return "ATOM";
            case "Avalanche X-Chain":
                return "AVAX";
            case "Avalanche C-Chain":
                return "AVAXC";
            case "BitcoinCash":
                return "BCH";
            case "TRC20":
                return "TRX";
            case "Conflux":
                return "CFX";
            case "CFX_EVM":
                return "CFXEVM";
            case "Cortex":
                return "CTXC";
            case "Decred":
                return "DCR";
            case "Digibyte":
                return "DGB";
            case "Elrond":
                return "EGLD";
            case "AELF":
                return "ELF";
            case "Enjin Relay Chain":
                return "ENJ";
            case "Ethereum Classic":
                return "ETC";
            case "EthereumPoW":
                return "ETHW";
            case "Filecoin":
                return "FIL";
            case "Fantom":
                return "FTM";
            case "Moonbeam":
                return "GLMR";
            case "Hedera":
                return "HBAR";
            case "ICON":
                return "ICX";
            case "MIOTA":
                return "IOTA";
            case "Kadena":
                return "KDA";
            case "Klaytn":
                return "KLAY";
            case "Kusama":
                return "KSM";
            case "Lisk":
                return "LSK";
            case "Litecoin":
                return "LTC";
            case "Terra Classic":
                return "LUNC";
            case "Terra":
                return "LUNA";
            case "Moonriver":
                return "MOVR";
            case "Harmony":
                return "ONE";
            case "Ontology":
                return "ONT";
            case "Quantum":
                return "QTUM";
            case "Ravencoin":
                return "RVN";
            case "Siacoin":
                return "SC";
            case "l-Stacks":
                return "STX";
            case "Celestia":
                return "TIA";
            case "New Economy Movement":
                return "XEM";
            case "Stellar Lumens":
                return "XLM";
            case "Tezos":
                return "XTZ";
            case "Zilliqa":
                return "ZIL";
            case "Polygon":
                return "MATIC";
            case "BRC20":
                return "ORDIBTC";
            case "Casper":
                return "CSPR";
            case "Polkadot":
                return "DOT";
            case "N3":
                return "NEO3";
            case "Flare":
                return "FLR";
            case "HyperCash":
                return "HC";
            case "PlatON":
                return "LAT";
            case "Ronin":
                return "RON";
            case "Khala":
                return "PHA";
            case "ONT":
                return "ONG";
            case "Omega Chain":
                return "OMG";
            case "OASYS":
                return "OAS";
        }
        return chain;
    }
}
