package com.example.backend.controller;


import com.example.backend.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final EmailSender emailSender;

    @Autowired
    public LoginController(EmailSender emailSender){
        this.emailSender = emailSender;
    }

    @GetMapping("/login")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/forgottenPW")
    public String forgottenPW() {
        return "forgottenPasswordPage";
    }
    @PostMapping("/forgottenPW")
    public String handleForgottenPassword(@RequestParam("email") String email) {
        String emailText = "tillfällig text - ändra senare";
        String subject = "Subject";
        emailSender.sendEmail(email, subject, emailText);

        return "loginPage";
    }
}