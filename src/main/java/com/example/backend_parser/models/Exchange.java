package com.example.backend_parser.models;

import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.parser.ApiCommunicator;
import com.example.backend_parser.parser.exchanges.BaseExchange;
import com.example.backend_parser.utils.BanListUtils;
import com.example.backend_parser.utils.RestartUtils;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Exchange {
    List<Token> tokens = new ArrayList<>();
    BaseExchange baseExchange;
    String name;
    String link;
    String linkSplitter;
    String splitter;
    String endOfLink = "";
    boolean toLowerCase;

    List<BlackListItem> blackList = new ArrayList<>();
    List<BanToken> banList = new ArrayList<>();
    ApiCommunicator apiCommunicator = new ApiCommunicator();

    public Exchange(String name, String link, String linkSplitter, String splitter, BaseExchange baseExchange) {
        this.name = name;
        this.link = link;
        this.linkSplitter = linkSplitter;
        this.splitter = splitter;
        this.baseExchange = baseExchange;
        parseBanTokens();
    }

    public List<BanToken> parseBanTokens() {
        List<BanToken> tokensBanned = apiCommunicator.getBanList(name);

        banList.clear();
        banList.addAll(tokensBanned);

        return tokensBanned;
    }

    public Exchange( String name, String link, String linkSplitter, String splitter, boolean toLowerCase, BaseExchange baseExchange) {
        this.name = name;
        this.link = link;
        this.linkSplitter = linkSplitter;
        this.splitter = splitter;
        this.toLowerCase = toLowerCase;
        this.baseExchange = baseExchange;
        parseBanTokens();
    }

    public Exchange( String name, String link, String linkSplitter, String splitter, boolean toLowerCase, BaseExchange baseExchange, String endOfLink) {
        this.name = name;
        this.link = link;
        this.linkSplitter = linkSplitter;
        this.splitter = splitter;
        this.toLowerCase = toLowerCase;
        this.baseExchange = baseExchange;
        this.endOfLink = endOfLink;
        parseBanTokens();
    }

    public void getBaseQuotes() {
        tokens.clear();
        List<Token> tokensOutput = baseExchange.getTradingPairs();
        List<Token> tokensToUpperCase = new ArrayList<>();
        for (Token token : tokensOutput) {

            if(!BanListUtils.tokenInBanList(token, this)) {
                Token token1 = token;
                token1.setBase(token1.getBase().toUpperCase());
                tokensToUpperCase.add(token1);
            }
        }
        tokens.addAll(tokensToUpperCase);
    }

    public void getChains() {
        try {
            baseExchange.getChains(baseExchange.requestChains(), tokens);
        } catch (IOException e) {
            RestartUtils.restartApp();
            throw new RuntimeException(e);
        }
    }

    public void getOrderBook(List<Token> tokensInp) {
        List<Token> tokensOutput = baseExchange.getOrderBooks(tokensInp, baseExchange.getMinAmount());
        for (Token symbol : tokensOutput) {
            for (Token token : tokensInp) {
                if (symbol.getSymbol().equalsIgnoreCase(token.getSymbol())) {
                    token.setBid(symbol.getBid());
                    token.setAsk(symbol.getAsk());
                }
            }
        }
        List<Token> tokens1 = new ArrayList<>(tokensInp);
        tokens.clear();
        tokens.addAll(tokens1);
    }

    public void setTokens(List<Token> tokensInput) {
        tokens.clear();
        tokens.addAll(tokensInput);
    }
}
