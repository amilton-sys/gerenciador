package com.sys.gerenciador.controller;

import com.sys.gerenciador.assembler.UserInputDesassembler;
import com.sys.gerenciador.core.ResourceUriHelper;
import com.sys.gerenciador.dto.ExpenseSummaryProjection;
import com.sys.gerenciador.dto.UserInput;
import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
public class HomeController {
    private final IUserService userService;
    private final IExpenseRepository iExpenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInputDesassembler userInputDesassembler;

    @GetMapping("/signin")
    public String navigateToLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String navigateToRegister() {
        return "register";
    }

    @GetMapping("/loadShopping")
    public String navigateToShopping() {
        return "user/add_shopping";
    }

    @GetMapping("/forgot-password")
    public String navigateToForgotPassword() {
        return "forgot_password";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model model, HttpServletRequest request) {
        if (p != null) {
            String email = p.getName();
            User userByEmail = userService.getUserByEmail(email);
            model.addAttribute("user", userByEmail);
        }
        model.addAttribute("path", request.getRequestURI());
    }

    @GetMapping("/")
    public String navigateToIndex(Model model) {
        List<ExpenseSummaryProjection> expenseSummaryProjections = iExpenseRepository.getAllExpenses();


        List<String> meses = List.of("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez");

        model.addAttribute("meses", meses);
        model.addAttribute("mesesValor", expenseSummaryProjections.stream().map(ExpenseSummaryProjection::getValotTotal).toList());

        return "index";
    }

//    @PostMapping("/forgot-password")
//    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
//        User userByEmail = userService.getUserByEmail(email);
//        if (ObjectUtils.isEmpty(userByEmail)) {
//            session.setAttribute("errorMsg", "Invalid email.");
//        } else {
//            String resetToken = UUID.randomUUID().toString();
//            userService.updateUserResetToken(email, resetToken);
//
//            String url = CommonUtils.generateUrl(request) + "/reset-password?token=" + resetToken;
//
//            Boolean sendMail = commonUtils.sendMail(url, email);
//            if (sendMail) {
//                session.setAttribute("succMsg", "Please check your email..Password Reset link send.");
//            } else {
//                session.setAttribute("errorMsg", "Something wrong on server ! Email not send.");
//            }
//        }
//        return "redirect:/forgot-password";
//    }

    @GetMapping("/reset-password")
    public String loadResetPassword(@RequestParam String token, Model model) {
        User userByToken = userService.getUserByToken(token);
        if (userByToken == null) {
            model.addAttribute("msg", "Your link is invalid or expired");
            return "message";
        }
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            model.addAttribute("msg", "Your link is invalid or expired.");
            return "message";
        } else {
            String passwordEncoded = passwordEncoder.encode(password);
            user.updatePassword(passwordEncoded);

            user.resetToken();

            userService.updateUser(user);

            model.addAttribute("msg", "Password change successfully.");
            return "message";
        }
    }

    @PostMapping("/saveUser")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody @Valid UserInput userInput) {
        var response = new HashMap<String, String>();
        User user = userInputDesassembler.toDomainObject(userInput);
        user = userService.saveUser(user);
        URI uri = ResourceUriHelper.buildUri(user);
        response.put("success", "true");
        response.put("message", "Usuário cadastrado com sucesso.");
        return ResponseEntity.created(uri).body(response);
    }

}
