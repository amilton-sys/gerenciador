package com.sys.gerenciador.util;

import com.sys.gerenciador.model.User;
import com.sys.gerenciador.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Component
public class CommonUtils {
//    @Autowired
//    private JavaMailSender mailSender;
    @Autowired
    private IUserService userService;

    public User getLoggedInUser(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    public static String generateUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.replace(request.getServletPath(), "");
    }
//
//    public Boolean sendMail(String url, String recipientEmail) throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom("amiltonjose.pe@gmail.com", "Gerenciar");
//        helper.setTo(recipientEmail);
//        String content = String.format(
//                """
//                     <p>Olá, %s </p>\s
//                     <p>Você solicitou a redefinição da sua senha.</p>\s
//                     <p>Clique no link abaixo para alterar sua senha:</p>\s
//                     <a href="%s"><p>Alterar minha senha</p></a>\s
//                \s""", recipientEmail, url);
//        helper.setSubject("Redefinição de senha");
//        helper.setText(content, true);
//        mailSender.send(message);
//        return true;
//    }
}
