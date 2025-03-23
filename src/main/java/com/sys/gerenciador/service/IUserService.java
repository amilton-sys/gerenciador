package com.sys.gerenciador.service;

import java.util.List;


import com.sys.gerenciador.model.User;

public interface IUserService {
    User saveUser(User user);
    User getUserByEmail(String email);
    List<User> getUsers(String role);
    Boolean updateAccountStatus(Long id, Boolean status);
    void increaseFailedAttempt(User user);
    void userAccountLock(User user);
    boolean unlockAccountTimeExpired(User user);
    void resetAttempt(Long usuarioId);
    void updateUserResetToken(String email, String resetToken);
    User getUserByToken(String token);
    User updateUser(User user);
    // Usuario updateUserProfile(Usuario user, MultipartFile file);
    Boolean existsEmail(String email); 
}
