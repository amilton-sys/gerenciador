package com.sys.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.gerenciador.model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    List<Usuario> findByRole(String role);

    Usuario findByResetToken(String token);

    Boolean existsByEmail(String email);
}
