package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.model.ShoppingItem;
import com.sys.gerenciador.repository.IShoppingRepository;
import com.sys.gerenciador.service.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterShoppingServiceImpl implements IGenericService<ShoppingItem> {
    @Autowired
    private IShoppingRepository iShoppingRepository;

    @Override
    public ShoppingItem save(ShoppingItem entity) {
        return iShoppingRepository.save(entity);
    }

    @Override
    public void remover(Long id) {
        iShoppingRepository.deleteById(id);
    }

    @Override
    public Optional<ShoppingItem> findById(Long id) {
        return iShoppingRepository.findById(id);
    }
}
