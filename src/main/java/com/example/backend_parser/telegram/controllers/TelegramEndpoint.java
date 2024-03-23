package com.example.backend_parser.telegram.controllers;

import com.example.backend_parser.telegram.services.TelegramService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

@RestController
@RequestMapping("${base-path}/telegram")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TelegramEndpoint {

    @PostMapping("/send_message")
    public HttpEntity<?> sendMessage(@RequestBody String message) throws IOException, InterruptedException {
        try {
            String decodedMessage = URLDecoder.decode(message, "UTF-8");
            TelegramService.getTelegram().sendMessage(decodedMessage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            TelegramService.getTelegram().sendMessage("Error in message decoding");
        }

        return new HttpEntity<>("OK");
    }
}
