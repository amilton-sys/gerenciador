package com.sys.gerenciador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountInput {
    @NotNull(message = "O Valor não pode estar em branco ou vazio.")
    @Positive(message = "O Valor não pode ser menor que R$ 1,00.")
    private BigDecimal salary;
}
