package com.verifime.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    @Inject
    public ExchangeRateCache(MeterRegistry meterRegistry) {
        this.cacheHitCounter = Counter.builder("exchange_rate_cache_hits_total")
                .description("Total number of exchange-rate cache hits")
                .register(meterRegistry);
        this.cacheMissCounter = Counter.builder("exchange_rate_cache_misses_total")
                .description("Total number of exchange-rate cache misses")
                .register(meterRegistry);
        Gauge.builder("exchange_rate_cache_entries", cache, Cache::estimatedSize)
                .description("Estimated number of exchange-rate cache entries")
                .register(meterRegistry);
    }

    public Map<String, BigDecimal> get(String key) {
        Map<String, BigDecimal> value = cache.getIfPresent(key);
        if (value == null) {
            cacheMissCounter.increment();
        } else {
            cacheHitCounter.increment();
        }
        return value;
    }

    public void put(String key, Map<String, BigDecimal> value) {
        cache.put(key, value);
    }
}
