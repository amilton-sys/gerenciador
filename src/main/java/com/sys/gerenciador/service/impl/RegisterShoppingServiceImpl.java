package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.model.Shopping;
import com.sys.gerenciador.repository.IShoppingRepository;
import com.sys.gerenciador.service.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterShoppingServiceImpl implements IGenericService<Shopping> {
    @Autowired
    private IShoppingRepository iShoppingRepository;

    @Override
    public Shopping save(Shopping entity) {
        return iShoppingRepository.save(entity);
    }

    @Override
    public void remover(Long id) {
        iShoppingRepository.deleteById(id);
    }

    @Override
    public Optional<Shopping> findById(Long id) {
        return iShoppingRepository.findById(id);
    }
}
