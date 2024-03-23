package com.example.backend_parser.api.controllers.private_path;

import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.api.services.BanTokenService;
import com.example.backend_parser.api.services.ExchangesService;
import com.example.backend_parser.api.websockets.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${base-path}/exchange")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExchangesEndpoint {
    @Autowired
    ExchangesService exchangesService;
    @Autowired
    MessageService messageService;
    @Autowired
    BanTokenService banTokenService;

    @GetMapping("/socket/exchanges")
    public void getExchangesAndSend() {
        messageService.sendMessageToClients(new HttpEntity<>(exchangesService.getExchanges()), "/topic/receive");
    }
    @GetMapping("/exchanges")
    public HttpEntity<?> getExchanges() {
        return new HttpEntity<>(exchangesService.getExchanges());
    }

    @GetMapping("/banList")
    public HttpEntity<List<BanToken>> banList(@RequestParam(name="exchange") String exchange) {
        return new HttpEntity<>(banTokenService.getByExchange(exchange));
    }

    @GetMapping("/ban")
    public HttpEntity<?> ban(@RequestParam(name = "token") String token, @RequestParam(name="exchange") String exchange) {
        banTokenService.addTokenToBanList(token, exchange);
//        messageService.sendMessageToClients(new HttpEntity<>(exchangesService.getExchanges()), "/topic/receive");
        return new HttpEntity<>("OK");
    }

    @GetMapping("/unban")
    public HttpEntity<?> unban(@RequestParam(name = "token") String token, @RequestParam(name="exchange") String exchange) {
        banTokenService.removeTokenFromBanList(token, exchange);
//        messageService.sendMessageToClients(new HttpEntity<>(exchangesService.getExchanges()), "/topic/receive");
        return new HttpEntity<>("OK");
    }
}
