package com.sys.gerenciador.controller;

import com.sys.gerenciador.assembler.ExpenseInputDesassembler;
import com.sys.gerenciador.dto.AmountInput;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public String loadExpenses(Model model,
                               @RequestParam(defaultValue = "0") Integer pageNumber,
                               @RequestParam(defaultValue = "6") Integer pageSize,
                               RedirectAttributes redirectAttributes) {

        if (pageNumber < 0)
            pageNumber = 0;
        if (pageSize <= 0 || pageSize > 100)
            pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "date"));
        Page<Expense> page = iExpenseRepository.findAll(pageable);

        if (pageNumber >= page.getTotalPages() && page.getTotalPages() > 0) {
            redirectAttributes.addAttribute("pageNumber", page.getTotalPages() - 1);
            redirectAttributes.addAttribute("pageSize", pageSize);
            return "redirect:/addExpenses";
        }

        model.addAttribute("pageNumber", page.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());

        List<Expense> expenses = page.getContent();

        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseSize", expenses.size());


        return "user/add_expenses";
    }

    @GetMapping("/removeExpense")
    public String removeExpense(@Param("id") Long id) {
        eRegisterExpense.remove(id);
        return "redirect:/addExpenses";
    }

    @PostMapping("/updateExpense")
    public String updateExpense(@ModelAttribute ExpenseComIdInput expenseComIdInput, HttpSession session,
                                BindingResult bindingResult) {

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
    public ResponseEntity<Map<String, String>> addExpense(@RequestBody @Valid ExpenseInput expenseInput) {
        Map<String, String> response = new HashMap<>();
        try {
            Expense expense = expenseInputDesassembler.toDomainObject(expenseInput);
            eRegisterExpense.save(expense);

            response.put("success", "true");
            response.put("message", "Despesa adicionada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", "Erro ao adicionar despesa.");
            return ResponseEntity.status(500).body(response);
        }
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
    public ResponseEntity<Map<String, String>> addAmount(@RequestBody AmountInput amountInput, Principal p) {
        Map<String, String> response = new HashMap<>();

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);
        if (usuarioLogado == null) {
            response.put("success", "false");
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        String amountStr = amountInput.getAmountStr();
        if (amountStr == null || amountStr.isBlank() || !isValidNumber(amountStr)) {
            response.put("success", "false");
            response.put("error", "Preencha corretamente o valor.");
            return ResponseEntity.badRequest().body(response);
        }

        BigDecimal amount = new BigDecimal(amountStr.replace(",", "."));
        usuarioLogado.setSalario(amount);
        userService.saveUser(usuarioLogado);

        response.put("success", "true");
        response.put("message", "Salário inserido com sucesso.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getSalary")
    public ResponseEntity<Map<String, String>> getSalary(Principal p) {
        Map<String, String> response = new HashMap<>();

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);
        if (usuarioLogado == null) {
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal salario = usuarioLogado.getSalario();

        response.put("salary", salario.toString());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getDebts")
    public ResponseEntity<Map<String, String>> getDebts(Principal p) {
        Map<String, String> response = new HashMap<>();

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);
        if (usuarioLogado == null) {
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

        Usuario usuarioLogado = commonUtils.getLoggedInUser(p);
        if (usuarioLogado == null) {
            response.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(401).body(response);
        }

        BigDecimal salario = usuarioLogado.getSalario() != null ? usuarioLogado.getSalario() : BigDecimal.ZERO;
        BigDecimal totalDividas = iExpenseRepository.amountExpensesActualMonth().orElse(BigDecimal.ZERO);

        BigDecimal sobras = salario.subtract(totalDividas);

        response.put("leftovers", sobras.toString());
        return ResponseEntity.ok(response);
    }


    private static boolean isValidNumber(String value) {
        return value.matches("^[0-9]+(\\.[0-9]{1,2})?$");
    }
}
