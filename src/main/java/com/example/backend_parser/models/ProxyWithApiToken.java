package com.example.backend_parser.models;

import com.example.backend_parser.proxy.ProxyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProxyWithApiToken {
    ProxyModel proxy;
    String apiToken;
    String wallet;

    public String getHost() {
        return proxy.getHost();
    }

    public int getPort() {
        return proxy.getPort();
    }

    public String getUsername() {
        return proxy.getUsername();
    }

    public String getPassword() {
        return proxy.getPassword();
    }
    @Override
    public String toString() {
        return "ProxyWithApiToken{" +
                "host='" + getHost() + '\'' +
                ", port=" + getPort() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", apiToken='" + apiToken + '\'' +
                ", wallet='" + wallet + '\'' +
                '}';
    }
}
