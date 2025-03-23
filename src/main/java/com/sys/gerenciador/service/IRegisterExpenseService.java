package com.sys.gerenciador.service;

import com.sys.gerenciador.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IRegisterExpenseService {
    void save(Long userId, Expense expense);

    void remove(Long id);

    Optional<Expense> findById(Long id);
    Page<Expense> findAllByUserId(Long userId, Pageable pageable);
}