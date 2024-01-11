package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.exchanges.BinanceExchange;
import com.example.backend_parser.exchanges.BybitExchange;
import com.example.backend_parser.exchanges.GateExchange;
import com.example.backend_parser.request.ProxyService;
import com.example.backend_parser.service.ReadProxiesService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BackendParserApplication {
	public static void main(String[] args) throws IOException {
		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);

//		Splitter.init();
//		try {
//			SpreadFinder.findSpreads();
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}

		GateExchange bybitExchange = new GateExchange();
		System.out.println(bybitExchange.requestChains());
	}

}
