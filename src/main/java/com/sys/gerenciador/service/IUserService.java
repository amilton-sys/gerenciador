package com.sys.gerenciador.service;

import java.util.List;


import com.sys.gerenciador.model.Usuario;

public interface IUserService {
    Usuario saveUser(Usuario usuario);
    Usuario getUserByEmail(String email);
    List<Usuario> getUsers(String role);
    Boolean updateAccountStatus(Long id, Boolean status);
    void increaseFailedAttempt(Usuario usuario);
    void userAccountLock(Usuario usuario);
    boolean unlockAccountTimeExpired(Usuario usuario);
    void resetAttempt(Long usuarioId);

    void updateUserResetToken(String email, String resetToken);
    Usuario getUserByToken(String token);
    Usuario updateUser(Usuario usuario);
    // Usuario updateUserProfile(Usuario user, MultipartFile file);
    Boolean existsEmail(String email); 
}
