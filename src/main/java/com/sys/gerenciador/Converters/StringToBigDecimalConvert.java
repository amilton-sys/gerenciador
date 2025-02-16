package com.sys.gerenciador.Converters;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToBigDecimalConvert implements Converter<String, BigDecimal> {

    @SuppressWarnings("null")
    @Override
    @Nullable
    public BigDecimal convert(String source) {
        return new BigDecimal(source.replace(",", "."));
    }

}
