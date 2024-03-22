package com.example.backend_parser.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Data@Builder@AllArgsConstructor@NoArgsConstructor
public class AuthResponse {
    private String token;
}
