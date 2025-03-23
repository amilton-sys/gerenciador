package com.sys.gerenciador.assembler;

import com.sys.gerenciador.dto.ShoppingInput;
import com.sys.gerenciador.model.ShoppingItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingInputDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public ShoppingItem toDomainObject(ShoppingInput shoppingInput) {
        return modelMapper.map(shoppingInput, ShoppingItem.class);
    }
    
    public void copyToDomainObject(ShoppingInput shoppingInput, ShoppingItem shopping){
        modelMapper.map(shoppingInput, shopping);
    }
}
