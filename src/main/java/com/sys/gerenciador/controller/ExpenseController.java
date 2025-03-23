package com.sys.gerenciador.controller;

import com.sys.gerenciador.assembler.ExpenseInputDesassembler;
import com.sys.gerenciador.dto.ExpenseComIdInput;
import com.sys.gerenciador.dto.ExpenseInput;
import com.sys.gerenciador.dto.ExpenseSummaryProjection;
import com.sys.gerenciador.model.Expense;
import com.sys.gerenciador.model.User;
import com.sys.gerenciador.repository.IExpenseRepository;
import com.sys.gerenciador.repository.IUserRepository;
import com.sys.gerenciador.service.IRegisterExpenseService;
import com.sys.gerenciador.util.AppConstant;
import com.sys.gerenciador.util.CommonUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ExpenseController {

    private final ExpenseInputDesassembler expenseInputDesassembler;
    private final IExpenseRepository iExpenseRepository;
    private final IUserRepository iUserRepository;
    private final IRegisterExpenseService iRegisterExpenseService;
    private final CommonUtils commonUtils;

    @GetMapping("/loadExpenses")
    public String loadExpenses(Model model,
                               @RequestParam(defaultValue = "0") Integer pageNumber,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               RedirectAttributes redirectAttributes) {

        if (pageNumber < 0)
            pageNumber = 0;
        if (pageSize <= 0 || pageSize > AppConstant.Pagination.MAX_PAGE_SIZE)
            pageSize = AppConstant.Pagination.DEFAULT_PAGE_SIZE;

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
        iRegisterExpenseService.remove(id);
        return "redirect:/addExpenses";
    }

    @PostMapping("/addExpense")
    public ResponseEntity<Map<String, String>> addExpense(@RequestBody @Valid ExpenseInput expenseInput, Principal p) {
        User user = commonUtils.getLoggedInUser(p);

        Map<String, String> response = new HashMap<>();
        try {
            Expense expense = expenseInputDesassembler.toDomainObject(expenseInput);
            iRegisterExpenseService.save(user.getId(), expense);

            response.put("success", "true");
            response.put("message", "Despesa adicionada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", "Erro ao adicionar despesa.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/updateExpense")
    public ResponseEntity<Map<String, String>> updateExpense(@RequestBody ExpenseComIdInput expenseComIdInput, Principal p) {
        User user = commonUtils.getLoggedInUser(p);
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Expense> expenseOptional = iRegisterExpenseService.findById(expenseComIdInput.getId());
            expenseInputDesassembler.copyToDomainObject(expenseComIdInput, expenseOptional.get());
            iRegisterExpenseService.save(user.getId(),expenseOptional.get());

            response.put("success", "true");
            response.put("message", "Despesa alterada com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", "Erro ao alterar despesa.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("expense/{id}")
    public ResponseEntity<Map<String, ?>> findExpense(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<Expense> expenseOptional = iRegisterExpenseService.findById(id);

            if (expenseOptional.isPresent()) {
                Expense expense = expenseOptional.get();
                Map<String, Object> expenseData = new HashMap<>();
                expenseData.put("id", expense.getId());
                expenseData.put("name", expense.getName());
                expenseData.put("value", expense.getValue());
                expenseData.put("date", expense.getDate().toString());
                return ResponseEntity.ok(expenseData);
            } else {
                response.put("success", "false");
                response.put("error", "Despesa não encontrada.");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", "Erro ao buscar despesa.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
