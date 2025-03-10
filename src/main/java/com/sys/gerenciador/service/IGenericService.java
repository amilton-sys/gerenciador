package com.sys.gerenciador.service;

import java.util.Optional;

public interface IGenericService<T> {
    T save(T entity);

    void remover(Long id);

    Optional<T> findById(Long id);
}
