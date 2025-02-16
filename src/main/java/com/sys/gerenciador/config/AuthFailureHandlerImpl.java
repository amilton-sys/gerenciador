package com.sys.gerenciador.config;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.service.IUserService;
import com.sys.gerenciador.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
    private final IUserRepository userRepository;
    private final IUserService userService;

    public AuthFailureHandlerImpl(IUserRepository userRepository, IUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        Usuario user = userRepository.findByEmail(email);

        if (exception instanceof BadCredentialsException) {
            exception = new BadCredentialsException("E-mail ou senha inválidos. Tente novamente.");
        }

        if (user != null) {
            if (user.getIsEnable()) {
                if (user.getAccountNonLocked()) {
                    if (user.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        userService.increaseFailedAttempt(user);
                    } else {
                        userService.userAccountLock(user);
                        exception = new LockedException("Sua conta está bloqueada. Tentativa falhada 3.");
                    }
                } else {
                    if (userService.unlockAccountTimeExpired(user)) {
                        exception = new LockedException("Sua conta está desbloqueada.Por favor, tente fazer login.");
                    } else {
                        exception = new LockedException("Sua conta está bloqueada. Por favor, tente mais tarde.");
                    }
                }
            } else {
                exception = new LockedException("Sua conta está inativa.");
            }
        } else {
            exception = new UsernameNotFoundException("E-mail ou Senha Invalida. Por favor, Registre-se.");
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
