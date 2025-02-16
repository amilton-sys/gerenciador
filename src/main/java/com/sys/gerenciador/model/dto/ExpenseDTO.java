package com.sys.gerenciador.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sys.gerenciador.model.Situacao;

import lombok.Data;

@Data
public class ExpenseDTO {
    private String nome;
    private BigDecimal valor;
    private LocalDate date;
    private Situacao situacao;
}
