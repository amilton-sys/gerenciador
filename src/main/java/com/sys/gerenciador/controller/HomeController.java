package com.sys.gerenciador.controller;

import com.sys.gerenciador.assembler.ExpenseInputDesassembler;
import com.sys.gerenciador.dto.ExpenseComIdInput;
import com.sys.gerenciador.dto.ExpenseInput;
import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.service.IRegisterExpenseService;
import com.sys.gerenciador.service.IUserService;
import com.sys.gerenciador.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final IExpenseRepository iExpenseRepository;
    private final IRegisterExpenseService eRegisterExpense;
    private final IUserService userService;
    private final CommonUtils commonUtils;
    private final ExpenseInputDesassembler expenseInputDesassembler;

    public HomeController(IExpenseRepository iExpenseRepository, IRegisterExpenseService eRegisterExpense,
                          IUserService userService, CommonUtils commonUtils, ExpenseInputDesassembler expenseInputDesassembler) {
        this.iExpenseRepository = iExpenseRepository;
        this.eRegisterExpense = eRegisterExpense;
        this.userService = userService;
        this.commonUtils = commonUtils;
        this.expenseInputDesassembler = expenseInputDesassembler;
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
        BigDecimal mesPassado = iExpenseRepository.amountExpensesPastMonth().orElse(BigDecimal.ZERO);
        BigDecimal mesAtual = iExpenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

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
    public String loadExpenses(Model model, Principal p, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber, 
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("date"));
        Page<Expense> page = iExpenseRepository.findAll(pageable);

        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        
        List<Expense> expenses = page.getContent();
                


        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseSize", expenses.size());

        BigDecimal totalDividas = iExpenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

        model.addAttribute("divida", totalDividas);

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);

        BigDecimal salario = usuarioLogado.getSalario() != null ? usuarioLogado.getSalario() : BigDecimal.ZERO;
        
        BigDecimal sobras = salario.subtract(totalDividas);

        if (sobras.compareTo(BigDecimal.ZERO) > 0) {
            model.addAttribute("sobras", sobras);
        } else {
            model.addAttribute("sobras", sobras.max(BigDecimal.ZERO));
        }

        return "user/add_expenses";
    }

    @GetMapping("/removeExpense")
    public String removeExpense(@Param("id") Long id) {
        eRegisterExpense.remove(id);
        return "redirect:/addExpenses";
    }

    @PostMapping("/updateExpense")
    public String updateExpense(@ModelAttribute ExpenseComIdInput expenseComIdInput, HttpSession session) {
        Optional<Expense> expenseOptional = eRegisterExpense.findById(expenseComIdInput.getId());
        expenseInputDesassembler.copyToDomainObject(expenseComIdInput, expenseOptional.get());
        Expense expenseEdited = eRegisterExpense.save(expenseOptional.get());
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
    public String addExpense(@ModelAttribute @Valid ExpenseInput expenseInput) {

        Expense expense = expenseInputDesassembler.toDomainObject(expenseInput);

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
