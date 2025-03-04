package com.sys.gerenciador.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Shopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String nome;
    private int quantidade;
    private BigDecimal valor;
}
