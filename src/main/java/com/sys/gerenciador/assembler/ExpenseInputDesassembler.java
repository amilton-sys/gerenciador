package com.sys.gerenciador.assembler;

import com.sys.gerenciador.dto.ExpenseInput;
import com.sys.gerenciador.model.Expense;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenseInputDesassembler {
    @Autowired
    private ModelMapper modelMapper;

    public Expense toDomainObject(ExpenseInput expenseInput) {
        return modelMapper.map(expenseInput, Expense.class);
    }
    
    public void copyToDomainObject(ExpenseInput expenseInput, Expense expense){
        modelMapper.map(expenseInput, expense);
    }
}
