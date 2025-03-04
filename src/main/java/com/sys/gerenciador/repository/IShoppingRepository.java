package com.sys.gerenciador.repository;

import com.sys.gerenciador.model.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingRepository extends JpaRepository<Shopping, Long> {
}
