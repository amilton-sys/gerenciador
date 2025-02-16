package com.sys.gerenciador.service;

import com.sys.gerenciador.model.Expense;

public interface IRegisterExpenseService {
    Expense save(Expense expense);

    void remove(Long id);

    Expense edit(Long id, Expense newExpense);
}