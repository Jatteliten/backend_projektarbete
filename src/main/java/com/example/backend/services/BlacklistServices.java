package com.example.backend.services;

import com.example.backend.model.Blacklist;

import java.util.List;

public interface BlacklistServices {
    public List<Blacklist> fetchBlacklist();
    public boolean isBlacklisted(String email);
    public void addPersonToBlacklist(String email, String name);
    public String updateBlacklistedPerson(String email, String newName, boolean newOkStatus);
    public List<Blacklist> filterBlacklist(String searchWord);
    public Blacklist findBlacklistObjById(Long id);
}
