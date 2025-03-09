package com.sys.gerenciador.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sys.gerenciador.model.Shopping;

public interface IShoppingRepository extends JpaRepository<Shopping, Long> {
    @Query(nativeQuery = true, value = "SELECT CAST(SUM(s.valor) AS DECIMAL(10,2)) FROM SHOPPING s WHERE YEAR(s.data) = YEAR(CURDATE()) AND MONTH(s.data) = MONTH(CURDATE())")
    Optional<BigDecimal> amountShoppingActualMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
