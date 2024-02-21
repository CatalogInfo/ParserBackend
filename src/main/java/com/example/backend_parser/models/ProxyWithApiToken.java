package com.example.backend_parser.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProxyWithApiToken {
    String host;
    int port;
    String username;
    String password;
    String apiToken;
    String wallet;

    @Override
    public String toString() {
        return "ProxyWithApiToken{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", apiToken='" + apiToken + '\'' +
                ", wallet='" + wallet + '\'' +
                '}';
    }
}
