package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.exchanges.*;
import com.example.backend_parser.mapper.exchanges.*;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;

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
		List<Token> tokens1 = Splitter.exchanges.get(0).getTokens();

		GateExchange gateExchange = new GateExchange();
		BybitExchange binanceExchange = new BybitExchange();

		GateMapper gateMapper = new GateMapper();
		BybitMapper binanceMapper = new BybitMapper();
		String response1 = binanceExchange.requestChains();
		String response = gateExchange.requestChains();
		binanceMapper.convertChains(response1, tokens1);
		gateMapper.convertChains(response, tokens);

		printUniqueChains(tokens, tokens1, "gate");
		printUniqueChains(tokens1, tokens, "bybit");

	}
	private static void printUniqueChains(List<Token> tokens1, List<Token> tokens2, String name) {
		Set<String> printedChains = new HashSet<>();

		for (Token token1 : tokens1) {
			for (Chain chain1 : token1.getChains()) {
				boolean isChainUnique = isChainUnique(chain1.getName(), tokens2);
				String combination = String.format("%s %s %s", name, chain1.getName(), token1.getBase() + " " + chain1.getFee());

				if (isChainUnique && !printedChains.contains(chain1.getName())) {
					System.out.println(combination);
					printedChains.add(chain1.getName());
				}
			}
		}
	}

	private static boolean isChainUnique(String chainName, List<Token> tokens) {
		for (Token token : tokens) {
			for (Chain chain : token.getChains()) {
				if (chain.getName().equalsIgnoreCase(chainName)) {
					return false;
				}
			}
		}
		return true;
	}

}
