package com.example.backend.services;

import com.example.backend.security.PasswordLink;

public interface PasswordLinkServices {
    String saveNewPassword(String email, String newPassword);
    String generateCreateNewPasswordLink(PasswordLink passwordLink);
    PasswordLink findByLinkKey(String linkKey);
    void saveNewPassWordLink(PasswordLink passwordLink);
}
