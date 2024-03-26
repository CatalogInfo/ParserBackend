package com.example.backend_parser.api.controllers.private_path;

import com.example.backend_parser.api.dtos.OptionsDto;
import com.example.backend_parser.api.responses.ParsingTimeResponse;
import com.example.backend_parser.api.services.MinAmountService;
import com.example.backend_parser.api.websockets.services.MessageService;
import com.example.backend_parser.api.services.OptionsService;
import com.example.backend_parser.parser.splitter.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${base-path}/general_info")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GeneralInfo {
    @Autowired
    MinAmountService minAmountService;
    @Autowired
    OptionsService optionsService;
    @Autowired
    MessageService messageService;

    @GetMapping("/parsingTime")
    public void getParsingTime() {
        messageService.sendMessageToClients(new HttpEntity<>(new ParsingTimeResponse(Splitter.parsingTime)), "/topic/parsingTime");
    }

    @GetMapping("/options")
    public HttpEntity<?> getOptions() {
        System.out.println("BUICTYU");
        return new HttpEntity<>(optionsService.getOptions());
    }

    @GetMapping("/default")
    public HttpEntity<?> getDefaultOptions() {
        return new HttpEntity<>(optionsService.getDefaultOptions());
    }

    @PostMapping("/options")
    public HttpEntity<?> setOptions(@RequestBody OptionsDto optionsDto) {
        optionsService.setOptions(optionsDto);
        return new HttpEntity<>("OK");
    }

    @GetMapping("/minAmount")
    public HttpEntity<Integer> minAmount() {
        return new HttpEntity<>(minAmountService.getMinAmount());
    }

    @PostMapping("/minAmount")
    public HttpEntity<?> setMinAmount(@RequestBody String minAmount) {
        minAmountService.setMinAmount(Integer.parseInt(minAmount));
        return new HttpEntity<>("OK");
    }
}
