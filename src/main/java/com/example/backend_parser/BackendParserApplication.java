package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.exchanges.*;
import com.example.backend_parser.mapper.exchanges.*;
import com.example.backend_parser.models.Chain;
import com.example.backend_parser.models.Token;
import com.example.backend_parser.splitter.Splitter;
import com.example.backend_parser.utils.RestartUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@SpringBootApplication
public class BackendParserApplication {
	public static void main(String[] args) throws IOException, AWTException {
//		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);

		Splitter.init();
		System.setProperty("java.awt.headless", "false");

//			while(true) {
				try {
					SpreadFinder.findSpreads();
				} catch (InterruptedException e) {
					RestartUtils.restartApp();
					throw new RuntimeException(e);
				}
//			}


		List<Token> tokens1 = Splitter.exchanges.get(0).getTokens();
		List<Token> tokens2 = Splitter.exchanges.get(1).getTokens();

		GateExchange exchange1 = new GateExchange();
		HuobiExchange exchange2 = new HuobiExchange();

		GateMapper mapper1 = new GateMapper();
		HuobiMapper mapper2 = new HuobiMapper();
		String response1 = exchange1.requestChains();
		String response2 = exchange2.requestChains();
		mapper1.convertChains(response1, tokens1);
		mapper2.convertChains(response2, tokens2);

		printUniqueChains(tokens1, tokens2, "gate");
		printUniqueChains(tokens2, tokens1, "huobi");

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
