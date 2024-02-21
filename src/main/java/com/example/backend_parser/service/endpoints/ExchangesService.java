package com.example.backend_parser.service.endpoints;

import com.example.backend_parser.SpreadFinder;
import com.example.backend_parser.entities.BanToken;
import com.example.backend_parser.exchanges.BaseExchange;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.responses.ChainResponse;
import com.example.backend_parser.responses.ExchangeResponse;
import com.example.backend_parser.dtos.OptionsDto;
import com.example.backend_parser.responses.TokenResponse;
import com.example.backend_parser.service.BanTokenService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExchangesService {

    @Autowired
    BanTokenService banTokenService;
    public List<ExchangeResponse> getExchanges() {
        List<Exchange> exchanges = Splitter.exchanges;

        List<ExchangeResponse> exchangeResponses = new ArrayList<>();

        for(Exchange exchange : exchanges) {
            exchangeResponses.add(new ExchangeResponse(exchange.getName(), getTokens(exchange), getBannedTokens(exchange)));
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

    public OptionsDto getOptions() {
        int minAmount = BaseExchange.MIN_AMOUNT;
        int minSpread = SpreadFinder.MIN_SPREAD;
        int maxSpread = SpreadFinder.MAX_SPREAD;

        return new OptionsDto(minAmount, minSpread, maxSpread);
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
            responses.add(new TokenResponse(token.getSymbol(), token.getBase(), token.getQuote(), getChains(token)));
        }

        return responses;
    }

}
