package com.example.backend.services.impl;

import com.example.backend.model.Blacklist;
import com.example.backend.services.BlacklistServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BlacklistServicesImpl implements BlacklistServices {

    private final String BLACKLIST_API_URL = "https://javabl.systementor.se/api/asmadali/blacklist";
    private final HttpClient httpClient;

    @Autowired
    public BlacklistServicesImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Blacklist> fetchBlacklist() {
        List<Blacklist> blacklist = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLACKLIST_API_URL))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                Blacklist[] blacklistArray = objectMapper.readValue(jsonResponse, Blacklist[].class);
                Collections.addAll(blacklist, blacklistArray);

            } else {
                System.out.println("Request failed, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        }
        return blacklist;
    }

    @Override
    public boolean isBlacklisted(String email) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLACKLIST_API_URL + "check/" + email))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                boolean isBlacklisted = jsonNode.get("ok").asBoolean();
                return !isBlacklisted;
            } else {
                System.out.println("Request failed, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void addPersonToBlacklist(String email, String name) {
        String jsonBody = "{\"email\":\"" + email + "\", \"name\":\"" + name + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLACKLIST_API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Mail added to blacklist");
            } else {
                System.out.println("Error when adding email to blacklist: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error when adding email to blacklist: " + e.getMessage());
        }
    }

    @Override
    public String updateBlacklistedPerson(String email, String newName, boolean newOkStatus) {
        String jsonBody = "{"
                + "\"name\":\"" + newName + "\", "
                + "\"ok\":" + newOkStatus
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLACKLIST_API_URL + "/" + email))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                return "Blacklisted person updated successfully.";
            } else {
                System.out.println("Error while updating blacklisted person. Status code:" + response.statusCode());

            }
        } catch (Exception e) {
            System.err.println("Error while updating blacklisted person: " + e.getMessage());
        }
        return "Error while updating blacklisted person.";
    }

    @Override
    public List<Blacklist> filterBlacklist(String searchWord) {
        List<Blacklist> matches = new ArrayList<>();
        List<Blacklist> allOnBlackList = fetchBlacklist();
        searchWord = searchWord.toLowerCase();

        for (Blacklist b : allOnBlackList) {
            if (
                    b.getId().toString().contains(searchWord) ||
                            b.getName().toLowerCase().contains(searchWord) ||
                            b.getEmail().toLowerCase().contains(searchWord)
            ) {
                matches.add(b);
            }
        }
        return matches;
    }

    @Override
    public Blacklist findBlacklistObjById(Long id) {
        for (Blacklist b : fetchBlacklist()) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

}
