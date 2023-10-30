package com.example.backend_parser.Telegram;

import lombok.Getter;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramService {

    static Telegram telegram = new Telegram();

    public static Telegram getTelegram() {
        return telegram;
    }
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
