package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.mapper.OkxMapper;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/okx")
public class OkxEndpoint {

    static final String ORDER_BOOK_URL = "https://www.okx.com/api/v5/market/books?instId=";
    static final String TRADING_PAIRS_URL = "https://www.okx.com/api/v5/public/instruments?instType=SPOT";
    static final Mapper MAPPER = new OkxMapper();

    ServiceEntity okxService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @GetMapping("/trading_pairs")
    public HttpEntity<String> getTradingPairs(){
        return okxService.parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {

        return okxService.parseOrderBooks(symbols, 1000);
    }

}
