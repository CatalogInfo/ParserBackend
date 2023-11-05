package com.example.backend_parser.Telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramService {

    static Telegram telegram = new Telegram();

    public static Telegram getTelegram() {
        return telegram;
    }

    @PostConstruct
    public static void registerBot() {
        try {
            TelegramBotsApi bot = new TelegramBotsApi(DefaultBotSession.class);

            bot.registerBot(telegram);
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
