package com.sys.gerenciador.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.util.AppConstant;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario saveUser(Usuario usuario) {
        usuario.setRole("ROLE_USER");
        usuario.setIsEnable(true);
        usuario.setAccountNonLocked(true);
        usuario.setFailedAttempt(0);
        String capitalizeFirstLetter = capitalizeFirstLetter(usuario.getName());
        usuario.setName(capitalizeFirstLetter);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return userRepository.save(usuario);
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public Usuario getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Long id, Boolean status) {
        Optional<Usuario> findUser = userRepository.findById(id);
        if (findUser.isPresent()) {
            Usuario usuario = findUser.get();
            usuario.setIsEnable(status);
            userRepository.save(usuario);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(Usuario usuario) {
        int attempt = usuario.getFailedAttempt() + 1;
        usuario.setFailedAttempt(attempt);
        userRepository.save(usuario);
    }

    @Override
    public void userAccountLock(Usuario usuario) {
        usuario.setAccountNonLocked(false);
        usuario.setLockTime(new Date());
        userRepository.save(usuario);
    }

    @Override
    public boolean unlockAccountTimeExpired(Usuario user) {
        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;
        long currentTime = System.currentTimeMillis();
        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(Long userId) {
        userRepository.findById(userId);
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        Usuario byEmail = userRepository.findByEmail(email);
        byEmail.setResetToken(resetToken);
        userRepository.save(byEmail);
    }

    @Override
    public Usuario getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public Usuario updateUser(Usuario user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
