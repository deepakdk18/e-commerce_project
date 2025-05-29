package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home - ECommerce");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login - ECommerce");
        return "login";
    }
}