package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.exchanges.*;
import com.example.backend_parser.mapper.exchanges.*;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.request.ProxyService;
import com.example.backend_parser.service.ReadProxiesService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class BackendParserApplication {
	public static void main(String[] args) throws IOException {
		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);

		Splitter.init();
		try {
			SpreadFinder.findSpreads();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		List<Token> tokens = Splitter.exchanges.get(1).getTokens();
		List<Token> tokens1 = Splitter.exchanges.get(0).getTokens();
//		System.out.println(tokens1);

//		GateExchange gateExchange = new GateExchange();
//		BinanceExchange binanceExchange = new BinanceExchange();
//
//		GateMapper gateMapper = new GateMapper();
//		BinanceMapper binanceMapper = new BinanceMapper();
//		String response1 = binanceExchange.requestChains();
//		String response = gateExchange.requestChains();
//		binanceMapper.convertChains(response1, tokens1);
//		gateMapper.convertChains(response, tokens);
//		HashMap<String, String> allChainsGate = new HashMap();
//		HashMap<String, String> allChainsBinance = new HashMap<>();
//
//		for(Token token : tokens) {
//			for(Chain chain : token.getChains()) {
//				allChainsGate.put(token.getBase(), chain.getName());
//			}
//		}
//
//		for(Token token : tokens1) {
//			for(Chain chain : token.getChains()) {
//				allChainsBinance.put(token.getBase(), chain.getName());
//			}
//		}
//
//
//		HashMap<String, String> result = new HashMap<>();
//		for(String gateChain : allChainsGate.keySet()) {
//			if(!allChainsBinance.containsValue(allChainsGate.get(gateChain))) {
//				result.put(allChainsGate.get(gateChain) + " " + gateChain, "gate");
//			}
//		}
//
//		for(String binanceChain : allChainsBinance.keySet()) {
//			if(!allChainsGate.containsValue(allChainsBinance.get(binanceChain))) {
//				result.put(allChainsBinance.get(binanceChain)  + " " + binanceChain, "binance");
//			}
//		}
//
//		for(String key : result.keySet()) {
//			System.out.println(result.get(key)+ " " + key);
//		}
	}
}
