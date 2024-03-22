package com.example.backend_parser.endpoints.public_path;

import com.example.backend_parser.request.AuthRequest;
import com.example.backend_parser.request.RegisterRequest;
import com.example.backend_parser.responses.AuthResponse;
import com.example.backend_parser.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println(request.getPassword());
        return ResponseEntity.ok(service.authenticate(request));
    }

}
