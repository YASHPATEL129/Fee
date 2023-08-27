package com.feeManagmentSystem.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Set your mail server host
        mailSender.setPort(587); // Set the port
        mailSender.setUsername("yashgondaliya1292@gmail.com"); // Set your username
        mailSender.setPassword("izcoeweqgdrpsbiu"); // Set your password

        // Set additional properties if needed
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        return mailSender;
    }
}
