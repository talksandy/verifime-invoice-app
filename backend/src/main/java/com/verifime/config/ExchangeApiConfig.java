package com.verifime.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "exchange.api")
public interface ExchangeApiConfig {
    String baseUrl();
    String endpoint();
    int timeout();
}
