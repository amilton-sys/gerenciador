package com.sys.gerenciador.service.impl;

import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        String capitalizeFirstLetter = capitalizeFirstLetter(user.getName());
        user.setName(capitalizeFirstLetter);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(Long id, Boolean status) {
        Optional<User> findUser = userRepository.findById(id);
        if (findUser.isPresent()) {
            User user = findUser.get();
            user.setIsEnable(status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(User user) {
        user.incrementFailedAttempt();
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(User user) {
        user.lock();
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(User user) {
        boolean unlocked = user.unlockAccountTimeExpired();
        if (unlocked) {
            userRepository.save(user);
        }
        return unlocked;
    }

    @Override
    public void resetAttempt(Long userId) {
        userRepository.findById(userId);
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User byEmail = userRepository.findByEmail(email);
        byEmail.setResetToken(resetToken);
        userRepository.save(byEmail);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
