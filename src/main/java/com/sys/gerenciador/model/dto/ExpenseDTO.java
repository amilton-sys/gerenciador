package com.sys.gerenciador.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseDTO {
    private String nome;
    private BigDecimal valor;
    private String categoria;
    private LocalDate data;
    private String situacao;
}
