package com.example.backend.services.impl;

import com.example.backend.configuration.IntegrationProperties;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlacklistServicesImpl implements BlacklistServices {

    private final HttpClient httpClient;

    IntegrationProperties integrationProperties;

    @Autowired
    public BlacklistServicesImpl(HttpClient httpClient, IntegrationProperties integrationProperties) {
        this.httpClient = httpClient;
        this.integrationProperties = integrationProperties;
    }

    public List<Blacklist> fetchBlacklist() {
        List<Blacklist> blacklist = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(integrationProperties.getBlacklist().getUrl()))
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
                .uri(URI.create(integrationProperties.getBlacklist().getUrl() + "check/" + email))
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
                throw new RuntimeException("Failed to check blacklist status, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
            throw new RuntimeException("Failed to check blacklist status: " + e.getMessage(), e);
        }
    }

    @Override
    public String addPersonToBlacklist(String email, String name) {

        String jsonBody = "{\"email\":\"" + email + "\", \"name\":\"" + name + "\"}";

        StringBuilder errorMess = new StringBuilder();

        boolean nameIsValid = validName(name);
        boolean emailIsValid = validEmail(email);

        if (!nameIsValid || !emailIsValid) {
            if (!nameIsValid) {
                errorMess.append("Invalid name");
            }
            if (!emailIsValid) {
                if (!errorMess.isEmpty()) {
                    errorMess.append("; ");
                }
                errorMess.append("Invalid email");
            }
            return errorMess.toString();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(integrationProperties.getBlacklist().getUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "Person added to blacklist successfully.";
            } else if (response.statusCode() == 400) {
                return "Email already on blacklist.";
            } else {
                return "Error when adding email to blacklist: " + response.statusCode();
            }
        } catch (Exception e) {
            return "Error when adding email to blacklist: " + e.getMessage();
        }

    }

    @Override
    public String updateBlacklistedPerson(String email, String newName, boolean newOkStatus) {
        String jsonBody = "{"
                + "\"name\":\"" + newName + "\", "
                + "\"ok\":" + newOkStatus
                + "}";

        if (!validName(newName)) {
            return "Invalid name";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(integrationProperties.getBlacklist().getUrl() + "/" + email))
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

    public boolean validEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validName(String name) {
        String nameRegex = "^[a-zA-ZàáâäãåąčćèéêëėįìíîïłńñòóôöõùúûüųūýÿżźžÀÁÂÄÃÅĄČĆÈÉÊËĖĮÌÍÎÏŁŃÑÒÓÔÖÕÙÚÛÜŲŪÝŸŻŹŽ'\\-\\s]+$";

        Pattern pattern = Pattern.compile(nameRegex);

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
