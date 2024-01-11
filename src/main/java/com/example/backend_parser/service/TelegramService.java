package com.example.backend_parser.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TelegramService {
    public static void sendMessage(String message) {
        try {
            String decodedMessage = URLDecoder.decode(message, "UTF-8");
            com.example.backend_parser.Telegram.TelegramService.getTelegram().sendMessage(decodedMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            com.example.backend_parser.Telegram.TelegramService.getTelegram().sendMessage("Error in message decoding");
        }

    }
}
