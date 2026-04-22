package com.verifime.service;

import com.verifime.cache.ExchangeRateCache;
import com.verifime.client.ResilientExchangeRateClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@ApplicationScoped
public class ExchangeRateFacade {

    @Inject ExchangeRateCache cache;
    @Inject ResilientExchangeRateClient resilientExchangeRateClient;

    private static final Logger LOG = Logger.getLogger(ExchangeRateFacade.class);

    public Map<String, BigDecimal> getRates(String base, LocalDate date) {

        String key = base + ":" + date;
        LOG.infof("ExchangeRateFacade:getRates - cached key is : %s", key);
        // 1. Cache first
        Map<String, BigDecimal> cached = cache.get(key);
        if (cached != null) {
            LOG.info("ExchangeRateFacade:getRates - getting data from cache");
            return cached;
        }

        // 2. API call
        LOG.info("ExchangeRateFacade:getRates - cache data not available, calling api to get data");
        Map<String, BigDecimal> rates = resilientExchangeRateClient.getRates(base, date);

        // 3. Store cache
        cache.put(key, rates);

        return rates;
    }
}
