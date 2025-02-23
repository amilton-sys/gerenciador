package com.sys.gerenciador.service;

import com.sys.gerenciador.model.Expense;

import java.util.Optional;

public interface IRegisterExpenseService {
    Expense save(Expense expense);

    void remove(Long id);

    Optional<Expense> findById(Long id);
}