package com.example.backend_parser.Telegram.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObtainCommand {
    String flag;
    String exchange;
}
