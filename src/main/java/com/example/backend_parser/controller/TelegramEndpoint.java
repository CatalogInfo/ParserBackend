package com.example.backend_parser.controller;

import com.example.backend_parser.Telegram.TelegramService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173"})
public class TelegramEndpoint {

    @PostMapping("/send_message")
    public HttpEntity<?> sendMessage(@RequestBody String message) throws IOException, InterruptedException {
        TelegramService.getTelegram().sendMessage(message);

        return new HttpEntity<>("OK");
    }
}
