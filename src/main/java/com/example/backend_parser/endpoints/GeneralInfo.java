package com.example.backend_parser.endpoints;

import com.example.backend_parser.responses.ParsingTimeResponse;
import com.example.backend_parser.service.endpoints.MessageService;
import com.example.backend_parser.splitter.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GeneralInfo {
    @Autowired
    MessageService messageService;
    @GetMapping("/parsingTime")
    public void getParsingTime() {
        messageService.sendMessageToClients(new HttpEntity<>(new ParsingTimeResponse(Splitter.parsingTime)), "/parsingTime/receive");
    }
}
