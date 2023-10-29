package com.example.backend_parser.controller;

import com.example.backend_parser.mapper.Mapper;
import com.example.backend_parser.models.BaseQuote;
import com.example.backend_parser.service.Service;
import com.example.backend_parser.service.ServiceEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bitrue")
public class BitrueEndpoint extends BaseEndpoint {
    static final String ORDER_BOOK_URL = "https://openapi.bitrue.com/api/v1/depth?symbol=";
    static final String TRADING_PAIRS_URL = "https://openapi.bitrue.com/api/v1/exchangeInfo";
    static final Mapper MAPPER = new Mapper();
    static final int DELAY_TIME = 100;

    ServiceEntity bitrueService = new ServiceEntity(ORDER_BOOK_URL, TRADING_PAIRS_URL, MAPPER);

    @Override
    protected Service getService() {
        return bitrueService;
    }

    @Override
    protected int getDelayTime() {
        return DELAY_TIME;
    }
}
