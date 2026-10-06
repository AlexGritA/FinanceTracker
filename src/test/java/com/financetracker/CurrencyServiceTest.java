package com.financetracker;

import com.financetracker.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyServiceTest {

    @Test
    void sameCurrencyReturnsAmountUnchanged() {
        CurrencyService currencyService = new CurrencyService(RestClient.create());

        Double result = currencyService.convert(100.0, "EUR", "EUR");

        assertEquals(100.0, result);
    }

    @Test
    void differentCurrencyConverts() {
        CurrencyService currencyService = new CurrencyService(RestClient.create());

        Double result = currencyService.convert(100.0, "EUR", "SEK");

        assertNotNull(result);
        assertTrue(result > 1000.0);
    }

}
