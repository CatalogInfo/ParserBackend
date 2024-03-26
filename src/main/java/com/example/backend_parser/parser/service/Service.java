package com.example.backend_parser.parser.service;

import com.example.backend_parser.http.AsyncRequestWithDelay;
import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.logs.LogFactory;
import com.example.backend_parser.parser.mapper.base.IMapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.utils.ThreadUtils;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class Service implements IExchangeService {
    @Override
    public List<Token> parseTradingPairs(String authToken) {
        IMapper mapper = getMapper();
        try {
            return mapper.convertBaseQuote(HttpClientMaker.get(getTradingPairsUrl(), null, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void parseChains(String response, List<Token> tokens) {
        getMapper().convertChains(response, tokens);
    }
    @Override
    public List<Token> parseOrderBooks(List<Token> tokens, int time, int minAmount, String authToken) {
        getTokens().addAll(runParseForOrderBooks(tokensToSymbols(tokens), time, minAmount, authToken));
        ArrayList<Token> finalList = new ArrayList<>(getTokens());
        clearInnerData();
        return finalList;
    }

    private List<String> tokensToSymbols(List<Token> token) {
        List<String> symbolList = token.stream()
                .map(Token::getSymbol)
                .collect(Collectors.toList());
        return symbolList;
    }

    private String getOrderBookUrlForSymbol(String symbol) {
        return getOrderBookUrl() + symbol + getAdditionalUrlParams();
    }

    @Override
    public Token parseOrderBookForToken(Token token, int minAmount) {
        try {
            return getMapper().convertResponseToToken(HttpClientMaker.get(
                    getOrderBookUrlForSymbol(token.getSymbol()), null, null),
                    token.getSymbol(),
                    minAmount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractSymbolFromLink(String url) {
        int startIndex = getOrderBookUrl().length();
        int endIndex = url.indexOf(getAdditionalUrlParams());

        if(endIndex == 0) {
            return url.substring(startIndex);
        }
        return url.substring(startIndex, endIndex);
    }

    private Token parseOrderBookForSymbol(String symbol, IMapper mapper, int minAmount, String response) {
        return mapper.convertResponseToToken(response, symbol, minAmount);
    }

    private List<Token> runParseForOrderBooks(List<String> symbols, int time, int minAmount, String authToken) {
        List<String> urls = new ArrayList<>();
        for (String symbol : symbols) {
            urls.add(getOrderBookUrlForSymbol(symbol));
        }
        LogFactory.makeALog("Started making requests");
        List<HttpResponse<String>> responses = AsyncRequestWithDelay.pollingGetRequest(urls, time, null, null);
        LogFactory.makeALog("Ending making requests");

        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < responses.size(); i ++) {
            Token token = parseOrderBookForSymbol(extractSymbolFromLink(urls.get(i)), getMapper(), minAmount, responses.get(i).body());
            tokens.add(token);
        }

        return tokens;
    }

    private void clearInnerData() {
        getThreads().clear();
        getTokens().clear();
    }

    abstract String getOrderBookUrl();
    abstract String getTradingPairsUrl();
    abstract String getAdditionalUrlParams();
    abstract List<Thread> getThreads();
    abstract List<Token> getTokens();
    abstract IMapper getMapper();
}
