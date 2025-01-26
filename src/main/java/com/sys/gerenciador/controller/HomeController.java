package com.sys.gerenciador.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sys.gerenciador.model.dto.ExpenseDTO;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
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
    public String addExpenses(Model model) {
        model.addAttribute("expense", new ExpenseDTO());
        ExpenseDTO expenseDTO1 = new ExpenseDTO();
        expenseDTO1.setNome("Amilton");
        ExpenseDTO expenseDTO2 = new ExpenseDTO();
        expenseDTO2.setNome("Amilton");
        ExpenseDTO expenseDTO3 = new ExpenseDTO();
        expenseDTO3.setNome("Amilton");
        model.addAttribute("expenses", List.of(expenseDTO1, expenseDTO2, expenseDTO3));
        return "add_expenses";
    }

    @GetMapping("/addShopping")
    public String addShopping() {
        return "add_shopping";
    }

    @PostMapping("/addExpense")
    public String postMethodName(@ModelAttribute ExpenseDTO expense,
            Model model) {

        System.out.println(expense);
        return "redirect:/addExpenses";
    }

}
