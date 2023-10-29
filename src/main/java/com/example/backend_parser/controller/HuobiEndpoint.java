package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.HuobiMapper;
import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/huobi")
@CrossOrigin(origins = "http://localhost:5174")
public class HuobiEndpoint {

    static final String ORDER_BOOK_URL = "https://api.huobi.pro/market/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://api.huobi.pro/v2/settings/common/symbols";
    static final Mapper MAPPER = new HuobiMapper();
    static final String ADDITIONAL_URL_PARAMS = "&type=step0";

    ServiceEntity huobiService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER, ADDITIONAL_URL_PARAMS);

    @GetMapping("/trading_pairs")
    public HttpEntity<List<BaseQuote>> getTradingPairs(){
        return huobiService.parseTradingPairs();
    }

    @PostMapping("/order_books")
    public HttpEntity<?> getOrderBooks(@RequestBody List<String> symbols) {

        return huobiService.parseOrderBooks(symbols, 1000);
    }

}
