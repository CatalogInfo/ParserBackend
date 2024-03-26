package com.example.backend_parser.api.services;

import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.api.repositories.BanTokenRepo;
import com.example.backend_parser.parser.splitter.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanTokenService implements IBanTokenService {

    @Autowired
    BanTokenRepo banTokenRepo;

    public void addTokenToBanList(String token, String exchange) {
        BanToken banToken = new BanToken();
        banToken.setToken(token);
        banToken.setExchange(exchange);

        if(!banTokenRepo.existsByTokenAndExchange(token, exchange)) {
            banTokenRepo.save(banToken);
        }
        Splitter.exchanges.forEach(exchange1 -> {
            if(exchange1.getName().equals(exchange)) {
                exchange1.parseBanTokens();
            }
        });
    }

    public List<BanToken> getByExchange(String exchange) {
        return banTokenRepo.findByExchange(exchange);
    }

    public void removeTokenFromBanList(String token, String exchange) {
        BanToken banToken = banTokenRepo.findByTokenAndExchange(token, exchange);

        banTokenRepo.delete(banToken);
    }

}
