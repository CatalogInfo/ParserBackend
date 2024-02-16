package com.example.backend_parser.endpoints;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send/message")
    @SendTo("/topic/receive")
    public String sendMessage(String message) {
        System.out.println(message);
        return message;
    }
}