package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.exchanges.BinanceExchange;
import com.example.backend_parser.exchanges.BitrueExchange;
import com.example.backend_parser.exchanges.BybitExchange;
import com.example.backend_parser.exchanges.GateExchange;
import com.example.backend_parser.mapper.exchanges.BinanceMapper;
import com.example.backend_parser.mapper.exchanges.BitrueMapper;
import com.example.backend_parser.mapper.exchanges.BybitMapper;
import com.example.backend_parser.mapper.exchanges.GateMapper;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.ProxyService;
import com.example.backend_parser.service.ReadProxiesService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendParserApplication {
	public static void main(String[] args) throws IOException {
//		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);

		Splitter.init();
		try {
			SpreadFinder.findSpreads();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		List<Token> tokens = Splitter.exchanges.get(1).getTokens();

		GateExchange binanceExchange = new GateExchange();
		BitrueExchange bitrueExchange = new BitrueExchange();

		GateMapper binanceMapper = new GateMapper();
		String response = binanceExchange.requestChains();
		binanceMapper.mapChains(response, tokens);
	}
}
