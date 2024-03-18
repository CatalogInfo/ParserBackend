package com.example.backend_parser.endpoints;

import com.example.backend_parser.dtos.OptionsDto;
import com.example.backend_parser.service.endpoints.ExchangesService;
import com.example.backend_parser.service.endpoints.MessageService;
import com.example.backend_parser.service.endpoints.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExchangesEndpoint {
    @Autowired
    ExchangesService exchangesService;
    @Autowired
    OptionsService optionsService;
    @Autowired
    MessageService messageService;
    @Autowired
    WebSocketController webSocketController;
    @GetMapping("/socket/exchanges")
    public void getExchangesAndSend() {
        System.out.println("HUI");
        messageService.sendMessageToClients(new HttpEntity<>(exchangesService.getExchanges()), "/topic/receive");
    }
    @GetMapping("/exchanges")
    public HttpEntity<?> getExchanges() {
        return new HttpEntity<>(exchangesService.getExchanges());
    }

    @GetMapping("/options")
    public HttpEntity<?> getOptions() {
        return new HttpEntity<>(optionsService.getOptions());
    }

    @GetMapping("/default")
    public HttpEntity<?> getDefaultOptions() {
        return new HttpEntity<>(optionsService.getDefaultOptions());
    }
    @PostMapping("/options")
    public HttpEntity<?> setOptions(@RequestBody OptionsDto optionsDto) {
        System.out.println(optionsDto);
        optionsService.setOptions(optionsDto);
        return new HttpEntity<>("OK");
    }
}
