package com.example.backend_parser.controller;

import com.example.backend_parser.Telegram.TelegramService;
import com.example.backend_parser.entities.BanToken;
import com.example.backend_parser.service.BanTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173"})
public class TelegramEndpoint {
    @Autowired
    BanTokenService banTokenService;

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

    @GetMapping("/banList")
    public HttpEntity<List<BanToken>> banList(@RequestParam(name="exchange") String exchange) {
        return new HttpEntity<>(banTokenService.getByExchange(exchange));
    }

    @GetMapping("/ban")
    public HttpEntity<?> ban(@RequestParam(name = "token") String token, @RequestParam(name="exchange") String exchange) {
        banTokenService.addTokenToBanList(token, exchange);

        return new HttpEntity<>("OK");
    }

    @GetMapping("/unban")
    public HttpEntity<?> unban(@RequestParam(name = "token") String token, @RequestParam(name="exchange") String exchange) {
        banTokenService.removeTokenFromBanList(token, exchange);

        return new HttpEntity<>("OK");
    }
}
