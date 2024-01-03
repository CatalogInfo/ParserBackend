package com.example.backend_parser.controller;


import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.IExchangeService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173"})
public abstract class BaseEndpoint {
    static final int MIN_AMOUNT = 2000;

    @GetMapping("/trading_pairs")
    public HttpEntity<List<BaseQuote>> getTradingPairs() {
        return getService().parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols, @RequestParam(name="min_amount", required = false) Integer minAmount) {
        if (minAmount == null)
            minAmount = MIN_AMOUNT;

        return getService().parseOrderBooks(symbols, getDelayTime(), minAmount);
    }

    protected abstract IExchangeService getService();

    protected abstract int getDelayTime();

}
