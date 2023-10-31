package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.exchanges.HuobiMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huobi")
public class HuobiEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api.huobi.pro/market/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://api.huobi.pro/v2/settings/common/symbols";
    static final IMapper MAPPER = new HuobiMapper();
    static final String ADDITIONAL_URL_PARAMS = "&type=step0";
    static final int DELAY_TIME = 100;

    ServiceEntity huobiService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER, ADDITIONAL_URL_PARAMS);

    @Override
    protected Service getService() {
        return huobiService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
