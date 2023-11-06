package com.example.backend_parser.Telegram;

import com.example.backend_parser.Telegram.entities.BanCommand;
import com.example.backend_parser.request.RequestMaker;
import org.springframework.stereotype.Service;
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

    public static void commandsHandler(String messageText, String chatId) {
        if(!defineIsCommand(messageText)) {
            getTelegram().sendMessageById("команда говна, смотри инструкцию: /info", chatId);
        }

        if(defineIsCommand(messageText)) {

            if (messageText.startsWith("/ban")) {
                BanCommand banCommand = getCommandBlocks(messageText);

                RequestMaker.getRequest("http://localhost:8080/ban?token=" + banCommand.getToken() + "&exchange=" + banCommand.getExchange());
                getTelegram().sendMessageById("чтобы разбанить - /unban " + banCommand.getExchange() + " " + banCommand.getToken(), chatId);
            } else if (messageText.startsWith("/unban")) {
                BanCommand banCommand = getCommandBlocks(messageText);

                RequestMaker.getRequest("http://localhost:8080/unban?token=" + banCommand.getToken() + "&exchange=" + banCommand.getExchange());
                getTelegram().sendMessageById("чтобы забанить - /ban " + banCommand.getExchange() + " " + banCommand.getToken(), chatId);
            } else if (messageText.equals("/info")) {
                getTelegram().sendMessageById("чтобы  забанить токен на бирже: напиши /ban exchange TOKENUSDT" + "\n"
                + "чтобы  разбанить токен на бирже: напиши /unban exchange TOKENUSDT" + "\n"
                + "*Объясняю* /ban/unban, название биржи маленьким шрифтом, токен вместе с приставкой USDT капсом и вместе", chatId);
            }
        }

    }

    private static boolean defineIsCommand(String messageText) {
        return messageText.startsWith("/");
    }

    private static BanCommand getCommandBlocks(String messageText) {
        String messageBody = messageText.substring(1);
        System.out.println(messageBody);
        String[] words = messageBody.split("\\s+");

        String flag = words[0];
        String exchange = words[1];
        String token = words[2];

        return new BanCommand(exchange, token, flag);
    }

}
