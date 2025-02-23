package com.sys.gerenciador.util;

import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class CommonUtils {
    @Autowired
    private IUserService userService;

    public Usuario getLoggedInUser(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }
}
