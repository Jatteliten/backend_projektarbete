package com.example.backend.services;

public interface BlacklistServices {
    public boolean isBlacklisted(String email);
    public void addEmailToBlacklist(String email);
    public void updateEmailInBlacklist(String oldEmail, String newEmail);
    public String getBlacklistIdByEmail(String email);
    public void removeEmailFromBlacklist(String email);
}
