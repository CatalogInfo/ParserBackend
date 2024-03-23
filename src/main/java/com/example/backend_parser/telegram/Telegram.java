package com.example.backend_parser.telegram;

import com.example.backend_parser.telegram.services.KeyboardService;
import com.example.backend_parser.telegram.services.TelegramService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

public class Telegram extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if(update.getMessage().getText().equals("/start")) {
            sendInfoButton(update.getMessage().getChatId());
        }
        String message_text = update.getMessage().getText();
        String chat_id = String.valueOf(update.getMessage().getChatId());

        TelegramService.commandsHandler(message_text, chat_id);
    }

    @Override
    public String getBotUsername() {
        return "parse_exchanges_bot";
    }

    @Override
    public String getBotToken() {
        return "5390306395:AAEe8Y1XzdgF8PvooDvet9Ul98Jy2kSUQIE";
    }

    public static List<String> chatID = Arrays.asList("549368505", "1664722747");

    public void sendMessage(String message) {
        for(int i = 0; i < chatID.size(); i ++) {
            sendMessageById(message, chatID.get(i));
        }
    }

    public void sendMessageById(String message, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendInfoButton(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Жмякни /info, чтобы понять, как взаимодействовать с ботом:");
        message.setReplyMarkup(KeyboardService.getInfoButton());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}