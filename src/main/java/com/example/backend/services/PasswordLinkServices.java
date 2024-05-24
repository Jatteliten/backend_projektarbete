package com.example.backend.services;

import com.example.backend.security.PasswordLink;

public interface PasswordLinkServices {
    String saveNewPassword(String email, String newPassword);
    String generateCreateNewPasswordLink(PasswordLink passwordLink);
    PasswordLink findByLinkKey(String linkKey);
    void savePassWordLink(PasswordLink passwordLink);
    void setPassWordLinkToUsed(PasswordLink passwordLink);
}
