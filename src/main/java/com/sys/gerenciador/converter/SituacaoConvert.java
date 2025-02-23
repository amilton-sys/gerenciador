package com.sys.gerenciador.converter;

import com.sys.gerenciador.model.Situacao;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SituacaoConvert implements Converter<String, Situacao> {

    @Override
    public Situacao convert(String source) {
        if (source.isBlank()) {
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
