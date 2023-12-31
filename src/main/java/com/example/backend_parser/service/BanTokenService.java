package com.example.backend_parser.service;

import com.example.backend_parser.entities.BanToken;
import com.example.backend_parser.repos.BanTokenRepo;
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
    }

    public List<BanToken> getByExchange(String exchange) {
        return banTokenRepo.findByExchange(exchange);
    }

    public void removeTokenFromBanList(String token, String exchange) {
        BanToken banToken = banTokenRepo.findByTokenAndExchange(token, exchange);

        banTokenRepo.delete(banToken);
    }

}
