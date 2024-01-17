package com.example.backend_parser.request;
import com.example.backend_parser.exchanges.OkxExchange;
import com.example.backend_parser.mapper.exchanges.OkxMapper;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class RequestUtils {
    public static String readFromConnection(InputStream inputStream){
        StringBuilder response = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;

            while (true) {
                if ((inputLine = in.readLine()) == null) break;
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static String getSignatureHmac256(String data, String key) {
        String HMAC_SHA256 = "HmacSHA256";
        byte[] hmacSha256;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return Hex.encodeHexString(hmacSha256);
    }

    public static String getSignatureHmac256(String key, String timestamp, String path) {
        String HMAC_SHA256 = "HmacSHA256";
        byte[] hmacSha256;
        String prehash = timestamp + "GET" + path;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(prehash.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return Base64.getEncoder().encodeToString(hmacSha256);
    }

    public static String getSignatureHmac256(String key, String timestamp, String api_key, String RECV_WINDOW) {
        String HMAC_SHA256 = "HmacSHA256";
        byte[] hmacSha256;
        String queryStr = timestamp + api_key + RECV_WINDOW;

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(queryStr.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return Hex.encodeHexString(hmacSha256);
    }

    public static String getSignatureHmac512(String key) {
        String data = "";
        String HMAC_SHA512 = "HmacSHA512";
        byte[] hmacSha512;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            hmacSha512 = mac.doFinal(data.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha512", e);
        }
        return Hex.encodeHexString(hmacSha512);
    }

}
