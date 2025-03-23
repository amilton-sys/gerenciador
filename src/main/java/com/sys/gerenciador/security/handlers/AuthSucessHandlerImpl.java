package com.sys.gerenciador.security.handlers;

import com.sys.gerenciador.model.User;
import com.sys.gerenciador.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {
    
    @Autowired
    @Lazy
    private IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getName();
        }

        log.info("Usuário '{}' fez login com sucesso em {}", username, LocalDateTime.now());

        User user = userService.getUserByEmail(username);
        if (user != null && user.hasFailedAttempts()) {
            user.resetFailedAttempts();
            userService.updateUser(user);
            log.info("Contador de tentativas falhas resetado para o usuário '{}'", username);
        }

        response.sendRedirect("/");
    }
}