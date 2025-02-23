package com.sys.gerenciador.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sys.gerenciador.model.Expense;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(nativeQuery = true, value = "SELECT CAST(SUM(e.valor) AS DECIMAL(10,2)) FROM EXPENSE e WHERE YEAR(e.date) = YEAR(CURDATE()) AND MONTH(e.date) = MONTH(CURDATE())")
    Optional<BigDecimal> amountExpensesActualMonth();

    @Query(nativeQuery = true, value = "SELECT * FROM EXPENSE e WHERE YEAR(e.date) = YEAR(CURDATE()) AND MONTH(e.date) = MONTH(CURDATE())")
    List<Expense> expensesActualMonth();

    @Query(nativeQuery = true, value = "SELECT CAST(SUM(e.valor) AS DECIMAL(10,2)) FROM EXPENSE e WHERE e.date >= DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01') AND e.date < DATE_FORMAT(NOW(), '%Y-%m-01')")
    Optional<BigDecimal> amountExpensesPastMonth();
}
