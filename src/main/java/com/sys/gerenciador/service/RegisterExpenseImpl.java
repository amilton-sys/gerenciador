package com.sys.gerenciador.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.repository.ExpenseRepository;

@Service
public class RegisterExpenseImpl implements IRegisterExpenseService {

    private final ExpenseRepository expenseRepository;

    public RegisterExpenseImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public void remove(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public Expense edit(Long id, Expense newExpense) {
        Optional<Expense> expenseDb = expenseRepository.findById(id);
        Expense expense = null;
        if (expenseDb.isPresent() && !ObjectUtils.isEmpty(expenseDb)) {
            BeanUtils.copyProperties(newExpense, expenseDb.get(),"id");

            expense = expenseRepository.save(expenseDb.get());
        }
        return expense;
    }

}
