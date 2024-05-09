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
        //blacklistServices.addEmailToBlacklist("@gmail.com");
        //blacklistServices.removeEmailFromBlacklist("lise@gmail.com");

        //boolean isBlacklisted = blacklistServices.isBlacklisted("hej@gmail.com");
        //System.out.println("isBlacklisted = " + isBlacklisted);

        //List<String> blacklist = blacklistServices.fetchBlacklist();
        //System.out.println(blacklist);

        //blacklistServices.updateEmailInBlacklist("lise@gmail.com", "lise.martinsen@gmail.com");
    }
    private final String BLACKLIST_API_URL = "https://javabl.systementor.se/api/asmadali/blacklist";

    /*
    FRÅGOR TILL STEFAN:
    - Är delete inte tillåtet? Statuskod 405
    - Kan man inte hämta enstaka värden? Måste man alltid få tillbaka ALLA svartlistade?
    - Är det tänkt att man alltid ska hämta ALL info från API:et för att sen i sitt service-lager
      själv sålla ut den informationen man letar efter? Känns som onödigt jobb.
     */

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


    /*
    Från början försökte jag hämta bara den specifika email-adressen
    för att slippa hämta allting. Verkar dock som att Stefan inte har
    skapat funktionalitet för att köra GET på enstaka värden.
    Fick tillbaka ALLA svartlistade när jag försökte hämta endast en person.
     */
    @Override
    public boolean isBlacklisted(String email) {
        List<String> blacklist = fetchBlacklist();
        for (String blacklistedEmail : blacklist) {
            if (blacklistedEmail.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addEmailToBlacklist(String email) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String jsonBody = "{\"email\":\"" + email + "\"}";
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
    public void updateEmailInBlacklist(String oldEmail, String newEmail) {
        String id = getBlacklistIdByEmail(oldEmail);

        //Funkar för tillfället inte eftersom det inte går att hämta enstaka värden
        if (id != null) {
            HttpClient httpClient = HttpClient.newHttpClient();
            String jsonBody = "{\"email\":\"" + newEmail + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BLACKLIST_API_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .method("PUT", HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    System.out.println("Email updated in blacklist");
                } else {
                    System.out.println("Error when updating email in blacklist: " + response.statusCode());
                }
            } catch (Exception e) {
                System.err.println("Error when updating email in blacklist: " + e.getMessage());
            }
        } else {
            System.out.println("No entry found in blacklist for email: " + oldEmail);
        }
    }

    @Override
    public String getBlacklistIdByEmail(String email) {
        //Vänta med tills fått reda på om man kan hämta enstaka värden eller inte från http.
        return null;
    }

    @Override
    public void removeEmailFromBlacklist(String email) {
        //FEL -- Statuskod 405 -> Metoden Delete är ej tillåten ---
        HttpClient httpClient = HttpClient.newHttpClient();
        String jsonBody = "{\"email\":\"" + email + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLACKLIST_API_URL))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Mail has been removed from blacklist");
            } else {
                System.out.println("Error when removing email from blacklist: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error when removing email from blacklist: " + e.getMessage());
        }
    }
}
