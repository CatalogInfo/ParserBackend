package com.example.backend_parser.encoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SignatureCalculator {
    String calculate(String key, String... data) throws NoSuchAlgorithmException, InvalidKeyException;
}
