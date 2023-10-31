package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.base.IMapper;
import com.example.backend_parser.mapper.exchanges.GateMapper;
import com.example.backend_parser.mapper.base.Mapper;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gate")
public class GateEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api.gateio.ws/api/v4/spot/order_book?currency_pair=";
    static final String TRADING_PAIRS_URL = "https://api.gateio.ws/api/v4/spot/currency_pairs";
    static final IMapper MAPPER = new GateMapper();
    static final int DELAY_TIME = 20;

    ServiceEntity gateService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @Override
    protected Service getService() {
        return gateService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
