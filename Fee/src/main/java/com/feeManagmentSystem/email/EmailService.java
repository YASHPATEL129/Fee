package com.feeManagmentSystem.email;


import com.feeManagmentSystem.entity.Student;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    public void sendEmailToStudent(Student student) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(student.getEmail());
        message.setSubject("Welcome to Our University");
        message.setText("Hello " + student.getName() + ",\n\n"
                + "Your student account has been created with the following details:\n"
                + "Username: " + student.getEmail() + "\n"
                + "Password: " + student.getPassword() + "\n\n"
                + "Please use this information to log in and access your account.\n\n"
                + "Thank you!");

        javaMailSender.send(message);
    }


    public void sendEmailsToAllStudents(List<Student> students) {
        for (Student student : students) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(student.getEmail());
            message.setSubject("Student Fee Details");

            String text = "Hello " + student.getName() + ",\n\n";

            if (student.getDue() == 0) {
                text += "Thank you for paying the full fee.\n";
            } else {
                text += "Here are your fee details:\n"
                        + "Total Fee: " + student.getFee() + "\n"
                        + "Paid Fee: " + student.getPaid() + "\n"
                        + "Due Fee: " + student.getDue() + "\n\n"
                        + "Please complete your payment as soon as possible.\n";
            }

            text += "Thank you!";

            message.setText(text);

            javaMailSender.send(message);
        }
    }
}
