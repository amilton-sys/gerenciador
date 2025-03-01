package com.sys.gerenciador.config.modelmapper;

import com.sys.gerenciador.dto.ExpenseInput;
import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.model.Situacao;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        var modelMapperTypeMap = modelMapper.createTypeMap(ExpenseInput.class,Expense.class);
//        modelMapperTypeMap.addMapping((src) -> src.getSituacao().getNome(),
//                Expense::setSituacao);
        return modelMapper;
    }
}
