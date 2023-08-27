package com.feeManagmentSystem.controller;

import com.feeManagmentSystem.entity.Accountant;
import com.feeManagmentSystem.entity.Admin;
import com.feeManagmentSystem.repository.AccountantRepository;
import com.feeManagmentSystem.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AccountantRepository accountantRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminRepository.findByUsernameAndPassword(username, password);
        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/home";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "home";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/"; // Redirect to the login page
    }

    @GetMapping("/add-accountant")
    public String showAddAccountantForm(Model model) {
        model.addAttribute("accountant", new Accountant());
        return "add_accountant"; // Show the add accountant form
    }

    @PostMapping("/add-accountant")
    public String addAccountant(@ModelAttribute Accountant accountant) {
        // Save the accountant details to the database using your repository
        accountantRepository.save(accountant);
        return "redirect:/admin/home"; // Redirect back to the home page
    }

    @PostMapping("/update-accountant")
    public String updateAccountant(@ModelAttribute Accountant updatedAccountant) {
        Long accountId = updatedAccountant.getId();

        if (accountId != null) {
            // Find the existing accountant by ID
            Optional<Accountant> existingAccountant = accountantRepository.findById(accountId);

            if (existingAccountant.isPresent()) {
                // Update the existing accountant's details
                Accountant accountant = existingAccountant.get();
                accountant.setName(updatedAccountant.getName());
                accountant.setEmail(updatedAccountant.getEmail());
                accountant.setContact(updatedAccountant.getContact());

                // Save the updated accountant to the database
                accountantRepository.save(accountant);
            }
        }

        return "redirect:/admin/view-accountants"; // Redirect back to the view accountants page
    }
}
