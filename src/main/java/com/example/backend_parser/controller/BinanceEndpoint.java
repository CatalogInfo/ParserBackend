package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/binance")
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173"})
public class BinanceEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api4.binance.com/api/v3/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://api4.binance.com/api/v3/exchangeInfo";
    static final IMapper MAPPER = new Mapper();

    static final int DELAY_TIME = 100;

    ServiceEntity binanceService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @Override
    protected Service getService() {
        return binanceService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}