package com.example.backend_parser.models;

import com.example.backend_parser.exchanges.BaseExchange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
    boolean toLowerCase;

    List<BlackListItem> blackList = new ArrayList<>();
    List<String> banList = new ArrayList<>();

    public Exchange(String name, String link, String linkSplitter, String splitter, BaseExchange baseExchange) {
        this.name = name;
        this.link = link;
        this.linkSplitter = linkSplitter;
        this.splitter = splitter;
        this.baseExchange = baseExchange;
    }

    public Exchange( String name, String link, String linkSplitter, String splitter, boolean toLowerCase, BaseExchange baseExchange) {
        this.name = name;
        this.link = link;
        this.linkSplitter = linkSplitter;
        this.splitter = splitter;
        this.toLowerCase = toLowerCase;
        this.baseExchange = baseExchange;
    }

    public void getBaseQuotes() {
        List<Token> tokensOutput = baseExchange.getTradingPairs();
        List<Token> tokensToUpperCase = new ArrayList<>();
        for (Token token : tokensOutput) {
            Token token1 = token;
            token1.setBase(token1.getBase().toUpperCase());
            tokensToUpperCase.add(token1);
        }
        tokens.addAll(tokensOutput);
    }

    public void getChains() {
        try {
            baseExchange.getChains(baseExchange.requestChains(), tokens);
        } catch (IOException e) {
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
