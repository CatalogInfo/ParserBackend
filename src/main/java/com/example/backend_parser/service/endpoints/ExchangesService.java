package com.example.backend_parser.service.endpoints;

import com.example.backend_parser.SpreadFinder;
import com.example.backend_parser.exchanges.BaseExchange;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.responses.ExchangeResponse;
import com.example.backend_parser.dtos.OptionsDto;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExchangesService {
    public List<ExchangeResponse> getExchanges() {
        List<Exchange> exchanges = Splitter.exchanges;

        List<ExchangeResponse> exchangeResponses = new ArrayList<>();

        for(Exchange exchange : exchanges) {
            exchangeResponses.add(new ExchangeResponse(exchange.getName(), exchange.getTokens().size(), getChains(exchange), getBannedTokens(exchange)));
        }
        return exchangeResponses;
    }

    public int getBannedTokens(Exchange exchange) {
        return exchange.getBanList().size();
    }

    public OptionsDto getOptions() {
        int minAmount = BaseExchange.MIN_AMOUNT;
        int minSpread = SpreadFinder.MIN_SPREAD;
        int maxSpread = SpreadFinder.MAX_SPREAD;

        return new OptionsDto(minAmount, minSpread, maxSpread);
    }

    private int getChains(Exchange exchange) {
        Set<String> chains = new HashSet<>();
        for(Token token : exchange.getTokens()) {
            for(Chain chain : token.getChains()) {
                chains.add(chain.getName());
            }
        }

        return chains.size();
    }

}
