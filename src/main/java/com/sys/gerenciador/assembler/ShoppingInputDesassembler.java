package com.sys.gerenciador.assembler;

import com.sys.gerenciador.dto.ShoppingInput;
import com.sys.gerenciador.model.Shopping;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingInputDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public Shopping toDomainObject(ShoppingInput shoppingInput) {
        return modelMapper.map(shoppingInput, Shopping.class);
    }
    
    public void copyToDomainObject(ShoppingInput shoppingInput, Shopping shopping){
        modelMapper.map(shoppingInput, shopping);
    }
}
