package com.verifime.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundingUtil {

    public static BigDecimal roundCurrency(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundFx(BigDecimal value) {
        return value.setScale(4, RoundingMode.HALF_UP);
    }
}
