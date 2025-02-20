package com.sys.gerenciador.service.impl;

import java.util.Optional;

import com.sys.gerenciador.service.IRegisterExpenseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.repository.IExpenseRepository;

@Service
public class RegisterExpenseImpl implements IRegisterExpenseService {

    private final IExpenseRepository IExpenseRepository;

    public RegisterExpenseImpl(IExpenseRepository IExpenseRepository) {
        this.IExpenseRepository = IExpenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        return IExpenseRepository.save(expense);
    }

    @Override
    public void remove(Long id) {
        IExpenseRepository.deleteById(id);
    }

    @Override
    public Expense edit(Long id, Expense newExpense) {
        Optional<Expense> expenseDb = IExpenseRepository.findById(id);
        Expense expense = null;
        if (expenseDb.isPresent() && !ObjectUtils.isEmpty(expenseDb)) {
            BeanUtils.copyProperties(newExpense, expenseDb.get(),"id");

            expense = IExpenseRepository.save(expenseDb.get());
        }
        return expense;
    }

}
