package com.example.backend.services.impl;

import com.example.backend.services.BlacklistServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BlacklistServicesImpl implements BlacklistServices {

    public static void main(String[] args) {
        BlacklistServicesImpl blacklistServices = new BlacklistServicesImpl();
        //blacklistServices.addEmailToBlacklist("stefanH@gmail.com", "Stefan Holmberg");
        //blacklistServices.removeEmailFromBlacklist("lise@gmail.com");

        //boolean isBlacklisted = blacklistServices.isBlacklisted("martin@gmail.com");
        //System.out.println("isBlacklisted = " + isBlacklisted);

        //List<String> blacklist = blacklistServices.fetchBlacklist();
        //System.out.println(blacklist);

        blacklistServices.updateBlacklistedPerson("lise@gmail.com", "Lise Martinsen", true);
    }

    private final String BLACKLIST_API_URL = "https://javabl.systementor.se/api/asmadali/blacklist";

    public List<String> fetchBlacklist() {
        List<String> blacklist = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://javabl.systementor.se/api/asmadali/blacklist"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                for (JsonNode node : jsonNode) {
                    String email = node.get("email").asText();
                    blacklist.add(email);
                }
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
        HttpClient httpClient = HttpClient.newHttpClient();
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
        HttpClient httpClient = HttpClient.newHttpClient();
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
    public void updateBlacklistedPerson(String email, String newName, boolean newOkStatus) {
        HttpClient httpClient = HttpClient.newHttpClient();
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
                System.out.println("Blacklisted person updated successfully.");
            } else {
                System.out.println("Error while updating blacklisted person. Status code:" + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error while updating blacklisted person: " + e.getMessage());
        }
    }

}
