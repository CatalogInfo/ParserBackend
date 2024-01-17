package com.example.backend_parser.mapper.base;

import com.example.backend_parser.calculations.PriceCalculator;
import com.example.backend_parser.models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.getJSONObject;
import static com.example.backend_parser.utils.JsonUtils.getValue;


public class Mapper extends Converter implements IMapper {
    protected List<String> resolvedQuoteAssets = Arrays.asList("USDT", "USD", "USDC");
    
    public Token convertResponseToToken(String response, String symbol, int minAmount) {
        BidsAsks orderBook = convertOrderBook(response);
        BidAsk bidAsk = PriceCalculator.calculatePriceForLiquidity(minAmount, orderBook);

        return new Token(symbol, bidAsk.getBid(), bidAsk.getAsk());
    }

    public void convertChains(String response, List<Token> tokens) {

    }

    public List<Token> convertBaseQuote(String response) {
        JSONArray symbols = getSymbols(response);

        List<Token> baseQuoteList = new ArrayList<>();

        for(int i = 0; i < symbols.length(); i ++) {
            JSONObject symbolObject = getJSONObject(symbols.get(i));

            String symbol = getValue(symbolObject, getKeysMapper().getSymbolKey());
            String baseAsset = getValue(symbolObject, getKeysMapper().getBaseKey());
            String quoteAsset = getValue(symbolObject, getKeysMapper().getQuoteKey());

            if(quoteAssetAndCustomCheck(symbolObject)) {
                baseQuoteList.add(new Token(symbol, baseAsset, quoteAsset));
            }
        }

        return baseQuoteList;
    }

    protected boolean quoteAssetAndCustomCheck(JSONObject symbolObject) {
        String quoteAsset = getValue(symbolObject, getKeysMapper().getQuoteKey());
        return resolvedQuoteAssets.contains(quoteAsset.toUpperCase());
    }
}