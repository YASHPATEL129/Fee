package com.feeManagmentSystem.controller;

import com.feeManagmentSystem.entity.Accountant;
import com.feeManagmentSystem.entity.Admin;
import com.feeManagmentSystem.repository.AccountantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/accountant")
public class AccountantLoginController {

    @Autowired
    private AccountantRepository accountantRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login_accountant";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, HttpSession session, Model model) {
        Accountant accountant = accountantRepository.findByNameAndPassword(name, password);
        if (accountant != null) {
            session.setAttribute("accountant", accountant);
            return "redirect:/accountant/home";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login_accountant";
        }
    }

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        if (session.getAttribute("accountant") != null) {
            return "accountant_home";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("accountant");
        return "redirect:/accountant/"; // Redirect to the login page
    }
}
