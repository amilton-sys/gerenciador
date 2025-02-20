package com.sys.gerenciador.util;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.service.IUserService;

@Component
public class CommonUtils {
    @Autowired
    private IUserService userService;

    public Usuario getLoggedInUser(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    public String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00"; 
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Valor inválido para formatação: " + value);
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormat.format(value);
    }
}
