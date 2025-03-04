package com.sys.gerenciador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ShoppingInput {
    @NotBlank(message = "O Nome não pode estar em branco")
    private String nome;
    @Positive(message = "A quantidade não pode ser menor que zero.")
    private int quantidade;
    @NotNull
    @Positive(message = "O Valor não pode ser menor que R$ 1,00.")
    private BigDecimal valor;
}
