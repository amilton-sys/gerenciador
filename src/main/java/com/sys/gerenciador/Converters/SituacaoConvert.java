package com.sys.gerenciador.Converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.sys.gerenciador.model.Situacao;

@Component
public class SituacaoConvert implements Converter<String, Situacao> {

    @SuppressWarnings("null")
    @Override
    @Nullable
    public Situacao convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        for (Situacao situacao : Situacao.values()) {
            if (situacao.getNome().equalsIgnoreCase(source)) {
                return situacao;
            }
        }
        throw new IllegalArgumentException("Situação inválida: " + source);
    }

}
