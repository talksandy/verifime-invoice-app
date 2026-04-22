package com.verifime.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.enterprise.context.ApplicationScoped;


import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@ApplicationScoped
public class ExchangeRateCache {

    private final Cache<String, Map<String, BigDecimal>> cache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .expireAfterWrite(Duration.ofHours(24))
                    .build();

    public Map<String, BigDecimal> get(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, Map<String, BigDecimal> value) {
        cache.put(key, value);
    }
}
