package com.sys.gerenciador.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Situacao {
    A_PAGAR("A Pagar"),
    PAGO("Pago");

    private final String nome;

    Situacao(String nome) {
        this.nome = nome;
    }

    @JsonCreator
    public static Situacao fromString(String nome) {
        return Stream.of(Situacao.values())
                .filter(situacao -> situacao.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Situacao inválida: " + nome));
    }
}
