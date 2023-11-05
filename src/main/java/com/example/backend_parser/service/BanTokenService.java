package com.example.backend_parser.service;

import com.example.backend_parser.entities.BanToken;
import com.example.backend_parser.repos.BanTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanTokenService implements IBanTokenService {

    @Autowired
    BanTokenRepo banTokenRepo;

    public void addTokenToBanList(String token, String exchange) {
        BanToken banToken = new BanToken();
        banToken.setToken(token);
        banToken.setExchange(exchange);

        banTokenRepo.save(banToken);
    }

    public void removeTokenFromBanList(String token, String exchange) {
        BanToken banToken = banTokenRepo.findByTokenAndExchange(token, exchange);

        banTokenRepo.delete(banToken);
    }

}
