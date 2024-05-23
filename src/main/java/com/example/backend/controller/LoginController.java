package com.example.backend.controller;


import com.example.backend.EmailSender;
import com.example.backend.security.PasswordLink;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import com.example.backend.services.PasswordLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.UUID;

@Controller
public class LoginController {

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    PasswordLinkServices passwordLinkServices;

    @Autowired
    public LoginController(EmailSender emailSender, UserRepository userRepository, PasswordLinkServices passwordLinkServices){
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.passwordLinkServices = passwordLinkServices;
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
    public String handleForgottenPassword(@RequestParam("username") String username) {
        User user = userRepository.getUserByUsername(username);
        if(user != null) {
            PasswordLink passwordLink = PasswordLink.builder()
                    .timeSent(LocalDate.now())
                    .user(user)
                    .linkKey(UUID.randomUUID().toString())
                    .build();
            passwordLinkServices.saveNewPassWordLink(passwordLink);
            String emailText = "To reset password, please use the following link: \n"
                    + passwordLinkServices.generateCreateNewPasswordLink(passwordLink);
            String subject = "Reset Password - Asmadali";
            emailSender.sendEmail(username, subject, emailText);

        }
        return "loginPage";
    }
}