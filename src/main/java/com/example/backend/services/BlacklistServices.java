package com.example.backend.services;

import com.example.backend.model.Blacklist;

import java.util.List;

public interface BlacklistServices {
    List<Blacklist> fetchBlacklist();
    boolean isBlacklisted(String email);
    void addPersonToBlacklist(String email, String name);
    String updateBlacklistedPerson(String email, String newName, boolean newOkStatus);
    List<Blacklist> filterBlacklist(String searchWord);
    Blacklist findBlacklistObjById(Long id);
}
