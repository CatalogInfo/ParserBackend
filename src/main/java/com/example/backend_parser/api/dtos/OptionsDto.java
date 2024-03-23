package com.example.backend_parser.api.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OptionsDto {
    int minAmount;
    int minSpread;
    int maxSpread;
    boolean checkChain;
}
