package com.example.backend_parser.encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSha512Calculator implements SignatureCalculator{
    private static final String HMAC_SHA512 = "HmacSHA512";
    private final Encoder encoder;

    public HmacSha512Calculator(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String calculate(String key, String... data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
        mac.init(secretKeySpec);
        StringBuilder prehash = new StringBuilder();
        for (String datum : data) {
            prehash.append(datum);
        }
        byte[] hmacSha512 = mac.doFinal(prehash.toString().getBytes());
        return encoder.encode(hmacSha512);
    }
}
