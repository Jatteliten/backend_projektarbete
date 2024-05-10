package com.example.backend.services;

public interface BlacklistServices {
    public boolean isBlacklisted(String email);
    public void addPersonToBlacklist(String email, String name);
    public void updateBlacklistedPerson(String email, String newName, boolean newOkStatus);
}
