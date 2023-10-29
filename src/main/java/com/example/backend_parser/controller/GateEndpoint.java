package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gate")
public class GateEndpoint {

    static final String ORDER_BOOK_URL = "https://api.gateio.ws/api/v4/spot/order_book?currency_pair=";
    static final String TRADING_PAIRS_URL = "https://api.gateio.ws/api/v4/spot/currency_pairs";
    static final Mapper MAPPER = new Mapper();

    ServiceEntity gateService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @GetMapping("/trading_pairs")
    public HttpEntity<String> getTradingPairs(){
        return gateService.parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {

        return gateService.parseOrderBooks(symbols, 1000);
    }

}
