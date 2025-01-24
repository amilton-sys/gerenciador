package com.sys.gerenciador.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/addExpenses")
    public String addExpenses() {
        return "add_expenses";
    }

    @GetMapping("/addShopping")
    public String addShopping() {
        return "add_shopping";
    }
}
