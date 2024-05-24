package com.example.backend.services.impl;

import com.example.backend.security.PasswordLink;
import com.example.backend.security.PasswordLinkRepository;
import com.example.backend.security.User;
import com.example.backend.security.UserRepository;
import com.example.backend.services.PasswordLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordLinkServicesImpl implements PasswordLinkServices{
    UserRepository userRepository;
    PasswordLinkRepository passwordLinkRepository;

    @Autowired
    public PasswordLinkServicesImpl(UserRepository userRepository,
                                    PasswordLinkRepository passwordLinkRepository){
        this.userRepository = userRepository;
        this.passwordLinkRepository = passwordLinkRepository;
    }

    @Override
    public String saveNewPassword(String email, String newPassword){
        User user = userRepository.getUserByUsername(email);
        if(user != null && newPassword.length() > 5){
            System.out.println("wiie");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hash = encoder.encode(newPassword);
            user.setPassword(hash);
            userRepository.save(user);
            return "success";
        }else{
            System.out.println("woo");
            return "password too short";
        }
    }

    @Override
    public String generateCreateNewPasswordLink(PasswordLink passwordLink){
        return "http://localhost:8080/newPassword/" + passwordLink.getLinkKey();
    }

    @Override
    public PasswordLink findByLinkKey(String linkKey){
        return passwordLinkRepository.findByLinkKey(linkKey);
    }

    @Override
    public void savePassWordLink(PasswordLink passwordLink){
        passwordLinkRepository.save(passwordLink);
    }
    @Override
    public void setPassWordLinkToUsed(PasswordLink passwordLink){
        passwordLink.setAlreadyUsed(true);
        passwordLinkRepository.save(passwordLink);
    }
}
