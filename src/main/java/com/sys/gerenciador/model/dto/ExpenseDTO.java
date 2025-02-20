package com.sys.gerenciador.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sys.gerenciador.model.Situacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseDTO {
    @NotBlank(message = "O Nome não pode estar em branco")
    private String nome;
    @NotNull
    @Positive(message = "O Valor não pode ser menor que R$ 1,00.")
    private BigDecimal valor;
    @NotNull(message = "O Data não pode estar vazia.")
    private LocalDate date;
    @NotNull(message = "A Situação não pode estar vazia.")
    private Situacao situacao;
}
