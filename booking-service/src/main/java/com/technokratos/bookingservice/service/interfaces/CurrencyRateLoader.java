package com.technokratos.bookingservice.service.interfaces;

import java.math.BigDecimal;

public interface CurrencyRateLoader {
    public void getCbrDailyCur();

    void updateUsdRate();

    BigDecimal getUsdRate();
}
