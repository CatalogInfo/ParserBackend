package com.example.backend_parser.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    String username;
    String password;
}
