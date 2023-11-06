package com.example.backend_parser.Telegram.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanCommand {
    String exchange;
    String token;
    String flag;
}
