package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.exchanges.BybitMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bybit")
public class BybitEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api.bybit.com/v5/market/orderbook?category=spot&symbol=";
    static final String TRADING_PAIRS_URL = "https://api.bybit.com/v5/market/instruments-info?category=spot";
    static final IMapper MAPPER = new BybitMapper();
    static final String ADDITIONAL_URL_PARAMS = "&limit=50";
    static final int DELAY_TIME = 100;

    ServiceEntity bybitService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER, ADDITIONAL_URL_PARAMS);

    @GetMapping("/trading_pairs")
    public HttpEntity<List<BaseQuote>> getTradingPairs(){
        return bybitService.parseTradingPairs();
    }

    @Override
    protected Service getService() {
        return bybitService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
