package com.example.backend_parser.Telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Telegram extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return "parse_exchanges_bot";
    }

    @Override
    public String getBotToken() {
        return "5390306395:AAEe8Y1XzdgF8PvooDvet9Ul98Jy2kSUQIE";
    }

    public static List<String> chatID = Arrays.asList("549368505", "639191552");

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID.get(0));
//        sendMessage.setChatId(chatID.get(1));
        sendMessage.setText(message);
        sendMessage.enableHtml(true);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}