package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mexc")
public class MexcEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api.mexc.com/api/v3/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://api.mexc.com/api/v3/exchangeInfo";
    static final IMapper MAPPER = new Mapper();
    static final int DELAY_TIME = 50;

    ServiceEntity mexcService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @Override
    protected Service getService() {
        return mexcService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
