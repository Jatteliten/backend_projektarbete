package com.example.backend.controller;

import com.example.backend.EmailSender;
import com.example.backend.security.PasswordLink;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import com.example.backend.services.PasswordLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.UUID;


@Controller
public class LoginController {

    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final PasswordLinkServices passwordLinkServices;

    @Autowired
    public LoginController(EmailSender emailSender, UserRepository userRepository, PasswordLinkServices passwordLinkServices) {
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
        if (user != null) {
            PasswordLink passwordLink = PasswordLink.builder()
                    .timeSent(LocalDate.now())
                    .user(user)
                    .linkKey(UUID.randomUUID().toString())
                    .alreadyUsed(false)
                    .build();
            passwordLinkServices.savePassWordLink(passwordLink);
            String emailText = "To reset password, please use the following link: \n"
                    + passwordLinkServices.generateCreateNewPasswordLink(passwordLink);
            String subject = "Reset Password - Asmadali";
            emailSender.sendEmail(username, subject, emailText);
        }
        return "loginPage";
    }

    @RequestMapping("/newPassword/{linkKey}")
    public String chooseNewPassword(@PathVariable String linkKey, Model model) {
        PasswordLink passwordLink = passwordLinkServices.findByLinkKey(linkKey);
        if (passwordLink != null && passwordLink.getTimeSent().isAfter(LocalDate.now().minusDays(1))
                && !passwordLink.isAlreadyUsed()) {
            model.addAttribute("linkKey", linkKey);
            return "changePassword";
        } else {
            return "loginPage";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String linkKey, @RequestParam String newPassword) {
        PasswordLink passwordLink = passwordLinkServices.findByLinkKey(linkKey);
        if (passwordLink != null && !passwordLink.isAlreadyUsed()) {
            passwordLink.setAlreadyUsed(true);
            passwordLinkServices.setPassWordLinkToUsed(passwordLink);
            passwordLinkServices.saveNewPassword(passwordLink.getUser().getUsername(), newPassword);
        }
        return "loginPage";
    }
}
