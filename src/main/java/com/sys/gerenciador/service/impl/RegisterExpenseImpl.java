package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.service.IRegisterExpenseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class RegisterExpenseImpl implements IRegisterExpenseService {

    private final IExpenseRepository iExpenseRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public RegisterExpenseImpl(IExpenseRepository iExpenseRepository) {
        this.iExpenseRepository = iExpenseRepository;
    }

    @Override
    @Transactional
    public Expense save(Expense expense) {
        entityManager.detach(expense);
        
        return iExpenseRepository.save(expense);
    }

    @Override
    public void remove(Long id) {
        iExpenseRepository.deleteById(id);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return iExpenseRepository.findById(id);
    }

}
