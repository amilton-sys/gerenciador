package com.sys.gerenciador.repository;

import com.sys.gerenciador.dto.ExpenseSummaryProjection;
import com.sys.gerenciador.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(nativeQuery = true, value = "SELECT CAST(SUM(e.value) AS DECIMAL(10,2)) FROM expense e WHERE YEAR(e.date) = YEAR(CURDATE()) AND MONTH(e.date) = MONTH(CURDATE())")
    Optional<BigDecimal> amountExpensesActualMonth();

    @Query(nativeQuery = true, value = "SELECT CAST(SUM(e.value) AS DECIMAL(10,2)) FROM expense e WHERE e.date >= DATE_FORMAT(NOW() - INTERVAL 1 MONTH, '%Y-%m-01') AND e.date < DATE_FORMAT(NOW(), '%Y-%m-01')")
    Optional<BigDecimal> amountExpensesPastMonth();

    @Query(nativeQuery = true, value =
            """
             SELECT
                DATE_FORMAT(e.date, '%Y-%m') as ano_mes,
                CAST(SUM(e.value) AS DECIMAL (10 , 2 )) AS valot_total
             FROM
                expense e
             WHERE YEAR(e.date) = YEAR(now())
             GROUP BY ano_mes
             ORDER BY ano_mes;
            """)
    List<ExpenseSummaryProjection> getAllExpenses();

    Page<Expense> findAllByUserId(Long userId, Pageable pageable);
}
