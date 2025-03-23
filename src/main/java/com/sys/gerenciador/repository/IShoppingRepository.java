package com.sys.gerenciador.repository;

import com.sys.gerenciador.model.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingRepository extends JpaRepository<ShoppingItem, Long> {
}
