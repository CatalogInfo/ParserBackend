package com.example.backend_parser.Telegram;

import com.example.backend_parser.Telegram.entities.BanCommand;
import com.example.backend_parser.Telegram.entities.ObtainCommand;
import com.example.backend_parser.models.Exchange;
import com.example.backend_parser.request.RequestMaker;
import com.example.backend_parser.splitter.Splitter;
import com.example.backend_parser.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

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
            } else if (messageText.startsWith("/list")) {
                ObtainCommand banCommand = getCommandBlocksForGet(messageText);
                String response = RequestMaker.getRequest("http://localhost:8080/banList?exchange=" + banCommand.getExchange());
                getTelegram().sendMessageById(getBannedTokensForExchange(response), chatId);
            } else if (messageText.startsWith("/exchanges")) {
                getTelegram().sendMessageById(getExchangesNames(), chatId);
            } else if (messageText.startsWith("/minAmount")) {
                String response = RequestMaker.getRequest("http://localhost:8080/minAmount");
                getTelegram().sendMessageById(response, chatId);
            } else if (messageText.startsWith("/set minAmount")) {
                String response = RequestMaker.postRequest("http://localhost:8080/minAmount", getMinAmount(messageText));
                getTelegram().sendMessageById(response, chatId);
            } else if (messageText.equals("/info")) {
                getTelegram().sendMessageById(
                " - чтобы  забанить токен на бирже: напиши /ban exchange TOKENUSDT" + "\n" + "\n"
                + " - чтобы  разбанить токен на бирже: напиши /unban exchange TOKENUSDT" + "\n" + "\n"
                + " - *Объясняю* /ban/unban, название биржи маленьким шрифтом, токен вместе с приставкой USDT, капсом и вместе" + "\n" + "\n"
                + " - Чтобы получить список забаненных токенов для какой-то биржи: /list bybit" + "\n" + "\n"
                + " - Чтобы получить текущий объём, по которой бот считает цену: /minAmount" + "\n" + "\n"
                + " - Чтобы установить новый минимальный объём: /set minAmount 2000" + "\n" + "\n"
                + " - Чтобы получить список работающих бирж: /exchanges" + "\n" + "\n",
                chatId);
            }
        }
    }

    private static boolean defineIsCommand(String messageText) {
        return messageText.startsWith("/");
    }

    private static BanCommand getCommandBlocks(String messageText) {
        String messageBody = messageText.substring(1);
        String[] words = messageBody.split("\\s+");

        String flag = words[0];
        String exchange = words[1];
        String token = words[2];

        return new BanCommand(exchange, token, flag);
    }

    private static String getBannedTokensForExchange(String response) {
        String bannedTokens = "";
        JSONArray arr = JsonUtils.getJSONArray(response);
        for(int i = 0; i < arr.length(); i ++) {
            JSONObject obj = arr.getJSONObject(i);
            String token = obj.getString("token");
            bannedTokens += token + "\n";
        }

        return bannedTokens;
    }

    private static String getMinAmount(String messageText) {
        String messageBody = messageText.substring(1);
        String[] words = messageBody.split("\\s+");

        String minAmount = words[2];
        return minAmount;
    }

    private static ObtainCommand getCommandBlocksForGet(String messageText) {
        String messageBody = messageText.substring(1);
        String[] words = messageBody.split("\\s+");

        String flag = words[0];
        String exchange = words[1];

        return new ObtainCommand(flag, exchange);
    }

    private static String getExchangesNames() {
        String names = "";
        for(Exchange exchange : Splitter.exchanges) {
            names = names + exchange.getName() + "\n";
        }

        return names;
    }

}
