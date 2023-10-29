package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.BybitMapper;
import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bybit")
public class BybitEndpoint {

    static final String ORDER_BOOK_URL = "https://api.bybit.com/v5/market/orderbook?category=spot&symbol=";
    static final String TRADING_PAIRS_URL = "https://api.bybit.com/v5/market/instruments-info?category=spot";
    static final Mapper MAPPER = new BybitMapper();
    static final String ADDITIONAL_URL_PARAMS = "&limit=50";

    ServiceEntity bybitService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER, ADDITIONAL_URL_PARAMS);

    @GetMapping("/trading_pairs")
    public HttpEntity<String> getTradingPairs(){
        return bybitService.parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {

        return bybitService.parseOrderBooks(symbols, 1000);
    }

}
