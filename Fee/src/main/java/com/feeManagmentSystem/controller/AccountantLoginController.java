package com.feeManagmentSystem.controller;

import com.feeManagmentSystem.email.EmailService;
import com.feeManagmentSystem.entity.Accountant;
import com.feeManagmentSystem.entity.Student;
import com.feeManagmentSystem.repository.AccountantRepository;
import com.feeManagmentSystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/accountant")
public class AccountantLoginController {

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String loginPage() {
        return "login_accountant";
    }

    @PostMapping("/authenticate")
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



    @GetMapping("/add-student")
    public String addStudentPage() {
        return "add_student"; // Return the HTML template name for adding student
    }

    @GetMapping("/view-students")
    public String viewStudentsPage(Model model) {
        List<Student> students = studentRepository.findAll(); // Fetch all students from the repository
        model.addAttribute("students", students);
        return "view_students";
    }


    @GetMapping("/send-email")
    public String sendEmailToAllStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        emailService.sendEmailsToAllStudents(students);
        model.addAttribute("message", "Emails sent successfully.");
        return "view_students"; // Redirect back to the view students page
    }

    @GetMapping("/delete-student/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id); // Delete student by ID
        return "redirect:/accountant/view-students"; // Redirect to the view page
    }

    @PostMapping("/update-student")
    public String updateStudent(@ModelAttribute Student updatedStudent) {
        Long studentId = updatedStudent.getId();

        if (studentId != null) {
            Optional<Student> existingStudent = studentRepository.findById(studentId);

            if (existingStudent.isPresent()) {
                Student student = existingStudent.get();
                student.setName(updatedStudent.getName());
                student.setEmail(updatedStudent.getEmail());
                student.setCourse(updatedStudent.getCourse());
                student.setFee(updatedStudent.getFee());
                student.setPaid(updatedStudent.getPaid());
                student.setAddress(updatedStudent.getAddress());
                student.setContact(updatedStudent.getContact());


                // Calculate and set the due fee
                double dueFee = student.getFee() - student.getPaid();
                student.setDue(dueFee);

                studentRepository.save(student);
            }
        }

        return "redirect:/accountant/view-students";
    }




}
