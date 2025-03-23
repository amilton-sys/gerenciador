package com.sys.gerenciador.controller;

import com.sys.gerenciador.dto.AmountInput;
import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.service.IUserService;
import com.sys.gerenciador.util.CommonUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    private final CommonUtils commonUtils;
    private final IExpenseRepository iExpenseRepository;


    @PostMapping("/addAmount")
    public ResponseEntity<Map<String, String>> addAmount(@RequestBody @Valid AmountInput amountInput, Principal p) {
        Map<String, String> response = new HashMap<>();

        User userLogado = commonUtils.getLoggedInUser(p);
        if (userLogado == null) {
            response.put("success", "false");
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal amount = amountInput.getSalary();

        userLogado.setSalary(amount);
        userService.saveUser(userLogado);

        response.put("success", "true");
        response.put("message", "Salário inserido com sucesso.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getSalary")
    public ResponseEntity<Map<String, String>> getSalary(Principal p) {
        Map<String, String> response = new HashMap<>();

        User userLogado = commonUtils.getLoggedInUser(p);
        if (userLogado == null) {
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal salario = userLogado.getSalary();

        response.put("salary", salario.toString());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getDebts")
    public ResponseEntity<Map<String, String>> getDebts(Principal p) {
        Map<String, String> response = new HashMap<>();

        User userLogado = commonUtils.getLoggedInUser(p);
        if (userLogado == null) {
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal totalDividas = iExpenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);
        response.put("debts", totalDividas.toString());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getleftovers")
    public ResponseEntity<Map<String, String>> getleftovers(Principal p) {
        Map<String, String> response = new HashMap<>();

        User userLogado = commonUtils.getLoggedInUser(p);
        if (userLogado == null) {
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal salario = userLogado.getSalary() != null ? userLogado.getSalary() : BigDecimal.ZERO;
        BigDecimal totalDividas = iExpenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

        BigDecimal sobras = salario.subtract(totalDividas);

        response.put("leftovers", sobras.toString());
        return ResponseEntity.ok(response);
    }

   
}
