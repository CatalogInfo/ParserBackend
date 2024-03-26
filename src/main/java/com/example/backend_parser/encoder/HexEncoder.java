package com.example.backend_parser.encoder;

import org.apache.commons.codec.binary.Hex;
public class HexEncoder implements Encoder {
    @Override
    public String encode(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
}