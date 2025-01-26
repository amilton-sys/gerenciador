package com.sys.gerenciador.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Expense {
    private Integer id;
    private String nome;
    private BigDecimal valor;
    private String categoria;
    private LocalDate date;
    private String situacao;
}
