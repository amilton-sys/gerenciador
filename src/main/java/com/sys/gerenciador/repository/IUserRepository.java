package com.sys.gerenciador.repository;

import java.util.List;

import com.sys.gerenciador.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByRole(String role);

    User findByResetToken(String token);

    Boolean existsByEmail(String email);
}
