package com.example.backend_parser.api.services;

import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.api.responses.ChainResponse;
import com.example.backend_parser.api.responses.ExchangeResponse;
import com.example.backend_parser.api.responses.TokenResponse;
import com.example.backend_parser.parser.splitter.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangesService {

    @Autowired
    BanTokenService banTokenService;
    public List<ExchangeResponse> getExchanges() {
        List<Exchange> exchanges = Splitter.exchanges;

        List<ExchangeResponse> exchangeResponses = new ArrayList<>();

        for(Exchange exchange : exchanges) {
            exchangeResponses.add(new ExchangeResponse(exchange.getLink(), exchange.getName(), getTokens(exchange), getBannedTokens(exchange), exchange.getSplitter()));
        }

        return exchangeResponses;
    }

    public List<String> getBannedTokens(Exchange exchange) {
        List<BanToken> tokens = banTokenService.getByExchange(exchange.getName());
        List<String> bannedTokensResponse = new ArrayList<>();

        for(BanToken banToken : tokens) {
            bannedTokensResponse.add(banToken.getToken());
        }
        return bannedTokensResponse;
    }

    private List<ChainResponse> getChains(Token token) {
        List<ChainResponse> responses = new ArrayList<>();

        for(Chain chain : token.getChains()) {
            responses.add(new ChainResponse(chain.getName(), chain.isDepositEnabled(), chain.isWithdrawalEnabled(), chain.getFee(), chain.getFeePercent()));
        }

        return responses;
    }

    private List<TokenResponse> getTokens(Exchange exchange) {
        List<TokenResponse> responses = new ArrayList<>();

        for(Token token : exchange.getTokens()) {
            responses.add(new TokenResponse(token.getSymbol(), token.getBase(), token.getQuote(), getChains(token), token.getBid(), token.getAsk()));
        }

        return responses;
    }

}
