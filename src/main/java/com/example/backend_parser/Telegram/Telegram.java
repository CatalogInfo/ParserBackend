package com.example.backend_parser.Telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

public class Telegram extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        String message_text = update.getMessage().getText();

        System.out.println(message_text);
        SendMessage message = new SendMessage() // Create a message object object
                .builder()
                .chatId("549368505")
                .text("Хуле баним?Все должно было работать...")
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "parse_exchanges_bot";
    }

    @Override
    public String getBotToken() {
        return "5390306395:AAEe8Y1XzdgF8PvooDvet9Ul98Jy2kSUQIE";
    }

    public static List<String> chatID = Arrays.asList("549368505", "639191552", "1664722747");

    public void sendMessage(String message) {
        for(int i = 0; i < chatID.size(); i ++) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID.get(i));
        sendMessage.setText(message);
        sendMessage.enableHtml(true);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        }


    }
}