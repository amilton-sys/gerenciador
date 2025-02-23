package com.sys.gerenciador.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class CurrencyUtil {
    public String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00";
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormat.format(value);
    }
}
