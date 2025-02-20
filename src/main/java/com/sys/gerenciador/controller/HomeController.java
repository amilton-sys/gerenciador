package com.sys.gerenciador.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.model.dto.ExpenseDTO;
import com.sys.gerenciador.repository.ExpenseRepository;
import com.sys.gerenciador.service.IRegisterExpenseService;
import com.sys.gerenciador.service.IUserService;
import com.sys.gerenciador.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    private final ExpenseRepository expenseRepository;
    private final IRegisterExpenseService eRegisterExpense;
    private final IUserService userService;
    private final CommonUtils commonUtils;

    public HomeController(ExpenseRepository expenseRepository, IRegisterExpenseService eRegisterExpense,
            IUserService userService, CommonUtils commonUtils) {
        this.expenseRepository = expenseRepository;
        this.eRegisterExpense = eRegisterExpense;
        this.userService = userService;
        this.commonUtils = commonUtils;
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model model, HttpServletRequest request) {
        if (p != null) {
            String email = p.getName();
            Usuario userByEmail = userService.getUserByEmail(email);
            model.addAttribute("user", userByEmail);
        }
        model.addAttribute("path", request.getRequestURI());
    }

    @GetMapping("/")
    public String index(Model model) {
        BigDecimal mesPassado = expenseRepository.amountExpensesPastMonth().orElse(BigDecimal.ZERO);
        BigDecimal mesAtual = expenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

        model.addAttribute("dataThisWeek", List.of(mesAtual));
        model.addAttribute("dataLastWeek", List.of(mesPassado));

        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/addExpenses")
    public String loadExpenses(Model model, Principal p) {

        model.addAttribute("expenseDTO", new ExpenseDTO());

        List<Expense> expenses = expenseRepository.findAll()
                .stream()
                .filter(expense -> expense.getDate() != null)
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .toList();

        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseSize", expenses.size());

        BigDecimal totalDividas = expenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

        model.addAttribute("divida", totalDividas);

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);

        BigDecimal sobras = usuarioLogado.getSalario() != null ? usuarioLogado.getSalario().subtract(totalDividas)
                : BigDecimal.ZERO;

        if (sobras.compareTo(BigDecimal.ZERO) > 0) {
            model.addAttribute("sobras", sobras);
        } else {
            model.addAttribute("sobras", 0);
        }

        return "user/add_expenses";
    }

    @GetMapping("/removeExpense")
    public String removeExpense(@Param("id") Long id) {
        eRegisterExpense.remove(id);
        return "redirect:/addExpenses";
    }

    @PostMapping("/updateExpense")
    public String updateExpense(@ModelAttribute Expense expense, HttpSession session) {
        Expense expenseEdited = eRegisterExpense.edit(expense.getId(), expense);
        if (!ObjectUtils.isEmpty(expenseEdited)) {
            session.setAttribute("succMsg", "Despesa atualizada com sucesso.");
        } else {
            session.setAttribute("errorMsg", "Atualização de depsesa falhou.");
        }
        return "redirect:/addExpenses";
    }

    @GetMapping("/addShopping")
    public String addShopping() {
        return "user/add_shopping";
    }

    @PostMapping("/addExpense")
    public String addExpense(@ModelAttribute @Valid ExpenseDTO expenseDTO, BindingResult bindingResult,
            HttpSession session, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("expenseDTO", expenseDTO);
            return "user/add_expenses";
        }

        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseDTO, expense);

        eRegisterExpense.save(expense);

        return "redirect:/addExpenses";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute Usuario usuario, HttpSession session) {
        Boolean existsEmail = userService.existsEmail(usuario.getEmail());
        if (existsEmail) {
            session.setAttribute("errorMsg", "Email already exist.");
        } else {
            userService.saveUser(usuario);
            session.setAttribute("succMsg", "User save success.");
        }
        return "redirect:/index";
    }

    @PostMapping("/addAmount")
    public String addAmount(@ModelAttribute("amountStr") String amountStr, Principal p, HttpSession session) {
        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);
        if (usuarioLogado != null) {
            if (!amountStr.isBlank() && IsNumber(amountStr)) {
                BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));
                usuarioLogado.setSalario(amount);
                userService.saveUser(usuarioLogado);
                session.setAttribute("succMsg", "Salário inserido com sucesso.");
            } else {
                session.setAttribute("errorMsg", "Preencha corretamente o valor.");
            }
        }
        return "redirect:/addExpenses";
    }

    private static boolean IsNumber(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
