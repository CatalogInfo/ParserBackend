package com.example.backend_parser;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendParserApplication {
	public static void main(String[] args) {
		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);

		Splitter.init();
		try {
			SpreadFinder.findSpreads();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
