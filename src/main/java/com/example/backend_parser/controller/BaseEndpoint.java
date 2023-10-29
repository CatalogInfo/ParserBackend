package com.example.backend_parser.controller;


import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.Service;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173"})
public abstract class BaseEndpoint {
    @GetMapping("/trading_pairs")
    public HttpEntity<List<BaseQuote>> getTradingPairs(){
        return getService().parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {
        return getService().parseOrderBooks(symbols, getDelayTime());
    }

    protected abstract Service getService();

    protected abstract int getDelayTime();

}
