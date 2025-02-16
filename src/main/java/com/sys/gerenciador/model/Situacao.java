package com.sys.gerenciador.model;

import lombok.Getter;

@Getter
public enum Situacao {
    A_PAGAR("A Pagar"),
    PAGO("Pago");

    private final String nome;

    Situacao(String nome) {
        this.nome = nome;
    }
}
