package com.sys.gerenciador.repository;

import com.sys.gerenciador.model.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingRepository extends JpaRepository<Shopping, Long> {
}
