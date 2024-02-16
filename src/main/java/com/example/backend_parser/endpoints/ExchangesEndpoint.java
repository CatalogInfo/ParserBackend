package com.example.backend_parser.endpoints;

import com.example.backend_parser.service.endpoints.ExchangesService;
import com.example.backend_parser.service.endpoints.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@CrossOrigin(allowedHeaders = "http://localhost:5173/")
public class ExchangesEndpoint {
    @Autowired
    ExchangesService exchangesService;
    @Autowired
    MessageService messageService;
    @Autowired
    WebSocketController webSocketController;
    @GetMapping("/socket/exchanges")
    public void getExchangesAndSend() {
        messageService.sendMessageToClients(new HttpEntity<>(exchangesService.getExchanges()));
    }
    @GetMapping("/exchanges")
    public HttpEntity<?> getExchanges() {
        return new HttpEntity<>(exchangesService.getExchanges());
    }
}
