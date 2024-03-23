package com.example.backend_parser.api.websockets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    SimpMessagingTemplate messagingTemplate;


    public void sendMessageToClients(HttpEntity<?> message, String url) {
        messagingTemplate.convertAndSend(url, message);
    }
}