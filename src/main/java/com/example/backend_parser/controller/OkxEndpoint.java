package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.mapper.exchanges.OkxMapper;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/okx")
public class OkxEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://www.okx.com/api/v5/market/books?instId=";
    static final String TRADING_PAIRS_URL = "https://www.okx.com/api/v5/public/instruments?instType=SPOT";
    static final IMapper MAPPER = new OkxMapper();
    static final String ADDITIONAL_URL_PARAMS = "&sz=100";
    static final int DELAY_TIME = 100;

    ServiceEntity okxService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER, ADDITIONAL_URL_PARAMS);

    @Override
    protected Service getService() {
        return okxService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
