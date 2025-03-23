package com.sys.gerenciador.security.handlers;

import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.service.IUserService;
import com.sys.gerenciador.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthFailureHandlerImpl.class);
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
        logger.warn("Tentativa de login malsucedida para o e-mail: {} em {}", email, LocalDateTime.now());
        logger.debug("Exceção original: {}", exception.getMessage());

        User user = userRepository.findByEmail(email);
        AuthenticationException customException;
        String failureUrl = "/signin?error";

        if (user == null) {
            customException = new UsernameNotFoundException("E-mail ou Senha Inválida. Por favor, Registre-se.");
            logger.info("Usuário não encontrado com o e-mail: {}", email);
            failureUrl = "/signin?error=notfound";
        } else if (user.isDisabled()) {
            customException = new LockedException("Sua conta está inativa.");
            logger.info("Tentativa de login em conta inativa: {}", email);
            failureUrl = "/signin?error=inactive";
        } else if (user.getAccountNonLocked()) {
            if (user.getFailedAttempt() < AppConstant.Security.MAX_FAILED_ATTEMPTS - 1) {
                userService.increaseFailedAttempt(user);
                long tentativasRestantes = AppConstant.Security.MAX_FAILED_ATTEMPTS - user.getFailedAttempt();
                customException = new BadCredentialsException("E-mail ou senha inválidos. Tentativa " +
                        user.getFailedAttempt() + " de " + AppConstant.Security.MAX_FAILED_ATTEMPTS +
                        ". Restam " + tentativasRestantes + " tentativas.");
                logger.info("Tentativa de login falha para usuário {}: {} de {} tentativas",
                        email, user.getFailedAttempt(), AppConstant.Security.MAX_FAILED_ATTEMPTS);
            } else {
                userService.userAccountLock(user);
                customException = new LockedException("Sua conta foi bloqueada devido a " +
                        AppConstant.Security.MAX_FAILED_ATTEMPTS + " tentativas falhas. Tente novamente em " +
                        (AppConstant.Security.UNLOCK_DURATION_TIME / 60000) + " minutos.");
                logger.warn("Conta bloqueada após tentativas máximas para o usuário: {}", email);
                failureUrl = "/signin?error=locked";
            }
        } else if (userService.unlockAccountTimeExpired(user)) {
            customException = new LockedException("Sua conta foi desbloqueada. Por favor, tente fazer login novamente.");
            logger.info("Conta desbloqueada automaticamente para o usuário: {}", email);
            failureUrl = "/signin?error=unlocked";
        } else {
            long lockTimeMillis = user.getLockTime().getTime();
            long unlockTimeMillis = lockTimeMillis + AppConstant.Security.UNLOCK_DURATION_TIME;
            long currentTimeMillis = System.currentTimeMillis();
            long minutesRemaining = (unlockTimeMillis - currentTimeMillis) / 60000;

            customException = new LockedException("Sua conta está bloqueada. Por favor, tente novamente em aproximadamente " +
                    minutesRemaining + " minutos.");
            logger.info("Tentativa de login em conta bloqueada: {}", email);
            failureUrl = "/signin?error=stillLocked";
        }

        super.setDefaultFailureUrl(failureUrl);
        super.onAuthenticationFailure(request, response, customException);
    }
}