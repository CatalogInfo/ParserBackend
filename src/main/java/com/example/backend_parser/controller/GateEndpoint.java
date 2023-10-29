package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.GateMapper;
import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gate")
public class GateEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://api.gateio.ws/api/v4/spot/order_book?currency_pair=";
    static final String TRADING_PAIRS_URL = "https://api.gateio.ws/api/v4/spot/currency_pairs";
    static final Mapper MAPPER = new GateMapper();
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
