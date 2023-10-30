package com.example.backend_parser;

import com.example.backend_parser.Telegram.Telegram;
import com.example.backend_parser.Telegram.TelegramService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BackendParserApplication {

	public static void main(String[] args) {
		TelegramService.registerBot();
		SpringApplication.run(BackendParserApplication.class, args);
	}
}
