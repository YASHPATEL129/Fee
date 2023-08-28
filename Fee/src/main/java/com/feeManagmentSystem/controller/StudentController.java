package com.feeManagmentSystem.controller;


import com.feeManagmentSystem.email.EmailService;
import com.feeManagmentSystem.entity.Student;
import com.feeManagmentSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/save-student")
    public String saveStudent(@ModelAttribute Student student) {
        // Calculate the due amount
        student.setDue(student.getFee() - student.getPaid());

        // Set the student fee status based on the fee and paid amounts
        if (student.getDue() <= 0) {
            student.setStudentFeeStatus("Paid");
        } else {
            student.setStudentFeeStatus("Not Paid");
        }

        // Save the student to the database
        studentRepository.save(student);

        return "redirect:/accountant/home"; // Redirect back to the accountant's home page
    }






}
