package com.example.backend_parser.api.services;

import com.example.backend_parser.api.table_entities.MinAmount;
import com.example.backend_parser.exchanges.BaseExchange;
import com.example.backend_parser.api.repositories.MinAmountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinAmountService {
    @Autowired
    MinAmountRepo minAmountRepo;

    public void setMinAmount(Integer minAmount) {
        BaseExchange.MIN_AMOUNT = minAmount;

        if (minAmountRepo.findAll().isEmpty()) {
            MinAmount minAmountLocal = new MinAmount();
            minAmountLocal.setMinAmount(minAmount);
            minAmountRepo.save(minAmountLocal);
        } else {
            MinAmount minAmountLocal = minAmountRepo.findAll().get(0);
            minAmountLocal.setMinAmount(minAmount);
            minAmountRepo.save(minAmountLocal);
        }

    }
    public Integer getMinAmount() {
        if (minAmountRepo.findAll().isEmpty()) {
            return 2000;
        }
        return minAmountRepo.findAll().get(0).getMinAmount();
    }
}
