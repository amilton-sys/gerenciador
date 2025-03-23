package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.service.IRegisterExpenseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sys.gerenciador.util.AppConstant.Time.MONTHS_PER_YEAR;

@Service
@AllArgsConstructor
public class RegisterExpenseServiceImpl implements IRegisterExpenseService {

    private final IExpenseRepository iExpenseRepository;
    private final IUserRepository iUserRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<Expense> findAllByUserId(Long userId, Pageable pageable) {
        return iExpenseRepository.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public void save(Long userId, Expense expense) {
        User user = iUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (expense.isRecurrence()) {
            List<Expense> expenses = new ArrayList<>();
            int actualMonth = LocalDate.now().getMonthValue();
            int months = MONTHS_PER_YEAR - actualMonth;
            Expense newExpense = new Expense();
            for (int i = 0; i < months; i++) {
                newExpense.setName(expense.getName());
                newExpense.setValue(expense.getValue());
                newExpense.setDate(expense.getDate().plusMonths(i));
                newExpense.setIsRecurring(true);
                newExpense.setCategory(expense.getCategory());
                newExpense.setUser(user);
                expenses.add(newExpense);
            }
            iExpenseRepository.saveAll(expenses);
        }else {
            expense.setUser(user);
            iExpenseRepository.save(expense);
        }
    }

    @Override
    public void remove(Long id) {
        iExpenseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Expense> findById(Long id) {
        return iExpenseRepository.findById(id);
    }

}
