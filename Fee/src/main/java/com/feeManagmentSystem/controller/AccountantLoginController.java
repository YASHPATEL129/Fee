package com.feeManagmentSystem.controller;

import com.feeManagmentSystem.email.EmailService;
import com.feeManagmentSystem.entity.Accountant;
import com.feeManagmentSystem.entity.Student;
import com.feeManagmentSystem.repository.AccountantRepository;
import com.feeManagmentSystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/accountant")
@AllArgsConstructor
public class AccountantLoginController {

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    private final JavaMailSender emailSender;




    @GetMapping("/")
    public String loginPage() {
        return "login_accountant";
    }

    @PostMapping("/authenticate")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Accountant accountant = accountantRepository.findByEmailAndPassword(email, password);
        if (accountant != null) {
            session.setAttribute("accountant", accountant);
            return "redirect:/accountant/home";
        } else {
            model.addAttribute("loginError", "Invalid email or password");
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

                // Set student fee status
                if (dueFee == 0) {
                    student.setStudentFeeStatus("paid");
                } else {
                    student.setStudentFeeStatus("unpaid");
                }

                studentRepository.save(student);
            }
        }

        return "redirect:/accountant/view-students";
    }

    @PostMapping("/send-email")
    @ResponseBody
    public ResponseEntity<String> sendEmailToStudents() {
        try {
            List<Student> students = studentRepository.findByStudentFeeStatus("Not Paid"); // Retrieve unpaid students from the repository

            for (Student student : students) {
                String subject = "Fee Reminder";
                String message = "Dear " + student.getName() + ",\n\n";
                message += "Your fee details:\n";
                message += "Total Fee: " + student.getFee() + "\n";
                message += "Paid Fee: " + student.getPaid() + "\n";
                message += "Due Fee: " + student.getDue() + "\n\n";
                message += "Please pay the fee as soon as possible.\n\n";
                message += "Regards,\nYour School";

                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(student.getEmail());
                email.setSubject(subject);
                email.setText(message);

                emailSender.send(email);
            }

            return ResponseEntity.ok("Emails sent successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails");
        }
    }
    @PostMapping("/send-paid-email")
    @ResponseBody
    public ResponseEntity<String> sendEmailToPaidStudents() {
        try {
            List<Student> students = studentRepository.findByStudentFeeStatus("paid");

            for (Student student : students) {
                String subject = "Fee Receipt Collection Reminder";
                String message = "Dear " + student.getName() + ",\n\n";
                message += "If you haven't received your fee receipt, please come to the admin office and collect it.\n";
                message += "If you have already collected your receipt, you can ignore this email.\n\n";
                message += "Regards,\nYour School";

                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(student.getEmail());
                email.setSubject(subject);
                email.setText(message);

                emailSender.send(email);
            }

            return ResponseEntity.ok("Emails sent successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending emails");
        }
    }
}