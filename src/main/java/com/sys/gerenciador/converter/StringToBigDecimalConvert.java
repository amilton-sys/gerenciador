package com.sys.gerenciador.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBigDecimalConvert implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String source) {
        return new BigDecimal(source.replace(",", "."));
    }

}
