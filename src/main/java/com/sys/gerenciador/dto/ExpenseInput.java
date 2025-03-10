package com.sys.gerenciador.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sys.gerenciador.model.Situacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseInput {
    @NotBlank(message = "O Nome não pode estar em branco")
    private String nome;
    @NotNull(message = "O Valor não pode estar em branco ou vazio.")
    @Positive(message = "O Valor não pode ser menor que R$ 1,00.")
    private BigDecimal valor;
    @NotNull(message = "O Data não pode estar vazia.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull(message = "A Situação não pode estar vazia.")
    private Situacao situacao;
}
