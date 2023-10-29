package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.GateMapper;
import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.mapper.MexcMapper;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mexc")
public class MexcEndpoint {

    static final String ORDER_BOOK_URL = "https://api.mexc.com/api/v3/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://api.mexc.com/api/v3/exchangeInfo";
    static final Mapper MAPPER = new MexcMapper();

    ServiceEntity mexcService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @GetMapping("/trading_pairs")
    public HttpEntity<String> getTradingPairs(){
        return mexcService.parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {

        return mexcService.parseOrderBooks(symbols, 1000);
    }

}
