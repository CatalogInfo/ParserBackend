package com.example.backend_parser.mapper.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeysMapper {
    private String symbolKey = "symbol";
    private String baseKey = "baseAsset";
    private String quoteKey = "quoteAsset";
    private String bidsKey = "bids";
    private String asksKey = "asks";

    public KeysMapper(String symbolKey, String baseKey, String quoteKey) {
        this.symbolKey = symbolKey;
        this.baseKey = baseKey;
        this.quoteKey = quoteKey;
    }

    public KeysMapper(String bidsKey, String asksKey) {
        this.bidsKey = bidsKey;
        this.asksKey = asksKey;
    }
}
