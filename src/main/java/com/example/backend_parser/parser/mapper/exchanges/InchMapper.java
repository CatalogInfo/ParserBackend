package com.example.backend_parser.parser.mapper.exchanges;

import com.example.backend_parser.parser.mapper.base.Mapper;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.backend_parser.utils.JsonUtils.getValue;

public class InchMapper extends Mapper {
    @Override
    public List<Token> convertBaseQuote(String response) {
        JSONObject jsonInp = JsonUtils.getJSONObject(response);
        List<Token> baseQuoteList = new ArrayList<>();

        Iterator i = jsonInp.keys();

        while (i.hasNext()) {
            JSONObject token = JsonUtils.getJSONObject(jsonInp, String.valueOf(i.next()));
            String baseAsset = getValue(token, "symbol");
            String address = getValue(token, "address");

            int decimals = Integer.parseInt(getValue(token, "decimals"));

            baseQuoteList.add(new Token(baseAsset + "_USDT", decimals, address, baseAsset, "USDT"));

        }
        return baseQuoteList;
    }
    @Override
    public void convertChains(String response, List<Token> tokens) {
        for(Token token : tokens) {
            token.addChain(new Chain("ETH", true, true, 0, 0));
        }
    }
}
