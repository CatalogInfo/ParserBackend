package com.example.backend_parser.service;

import com.example.backend_parser.entities.MinAmount;
import com.example.backend_parser.repos.MinAmountRepo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinAmountService {
    @Autowired
    MinAmountRepo minAmountRepo;

    public void setMinAmount(Integer minAmount) {
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
