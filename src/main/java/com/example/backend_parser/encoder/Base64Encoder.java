package com.example.backend_parser.encoder;
import java.util.Base64;

public class Base64Encoder implements Encoder {
    @Override
    public String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}