package com.feeManagmentSystem.controller;

import com.feeManagmentSystem.entity.Accountant;
import com.feeManagmentSystem.entity.Student;
import com.feeManagmentSystem.repository.AccountantRepository;
import com.feeManagmentSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class AccountantController {

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private StudentRepository studentRepository;

    private JavaMailSender javaMailSender;

    @GetMapping("/admin/view-accountants")
    public String viewAccountants(Model model) {
        List<Accountant> accountants = accountantRepository.findAll();
        model.addAttribute("accountants", accountants);
        return "view_accountants"; // Return the HTML template name
    }

    @GetMapping("/admin/edit-accountant/{id}")
    public String showEditAccountantForm(@PathVariable Long id, Model model) {
        Optional<Accountant> accountant = accountantRepository.findById(id);
        if (accountant.isPresent()) {
            model.addAttribute("accountant", accountant.get());
            return "edit_accountant";
        } else {
            return "redirect:/admin/view-accountants";
        }
    }

    @GetMapping("/admin/delete-accountant/{id}")
    public String deleteAccountant(@PathVariable Long id) {
        accountantRepository.deleteById(id);
        return "redirect:/admin/view-accountants";
    }

    @GetMapping("/accountant/edit-student/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "edit_student"; // Return the edit student form view
        } else {
            return "redirect:/accountant/view-students";
        }
    }


}
