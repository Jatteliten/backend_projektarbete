package com.example.backend.services;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface SendEmailServices {
    void sendConfirmationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
