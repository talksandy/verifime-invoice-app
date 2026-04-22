package com.verifime.client.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifime.dto.RateResponse;
import com.verifime.exception.ExchangeRateException;
import com.verifime.util.RoundingUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FrankfurterResponseMapper {

    @Inject
    ObjectMapper objectMapper;
    /**
     * Converts external API response (array format)
     * into internal domain model (Map<String, BigDecimal>)
     */

    public Map<String, BigDecimal> toRatesMap(String responseBody) {

        try {
            RateResponse[] ratesArray = objectMapper.readValue(responseBody, RateResponse[].class);

            if (ratesArray == null || ratesArray.length == 0) {
                throw new ExchangeRateException("No exchange rates returned from API");
            }

            Map<String, BigDecimal> rates = new HashMap<>();

            for (RateResponse rate : ratesArray) {
                if (rate.quote() == null || rate.rate() == null) {
                    continue; // skip bad data
                }
                rates.put(
                        rate.quote(),
                        RoundingUtil.roundFx(rate.rate())
                );
            }
            return rates;
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Exception in processing json ", exception);
        }
    }
}
