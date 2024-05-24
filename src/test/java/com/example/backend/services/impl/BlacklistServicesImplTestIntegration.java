package com.example.backend.services.impl;

import com.example.backend.HttpClientConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BlacklistServicesImplTestIntegration {

    @Autowired
    private HttpClient httpClient;

   @Value("${integrations.blacklist.testUrl}")
    private String blacklistUrl;

   @Value("${integrations.blacklist.testUrlEmailCheck}")
    private String blacklistUrlCheckEmail;


    @Test
    void restApiReturnsCorrectJsonStructure() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(blacklistUrl))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();

        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        assertTrue(jsonElement.isJsonArray(), "The response is not a JSON array");

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            assertTrue(element.isJsonObject(), "Array element is not a JSON object");

            JsonObject jsonObject = element.getAsJsonObject();
            assertNotNull(jsonObject.get("id"), "id field is missing");
            assertTrue(jsonObject.get("id").isJsonPrimitive() && jsonObject.get("id").getAsJsonPrimitive().isNumber(), "id field is not a number");

            assertNotNull(jsonObject.get("email"), "email field is missing");
            assertTrue(jsonObject.get("email").isJsonPrimitive() && jsonObject.get("email").getAsJsonPrimitive().isString(), "email field is not a string");

            assertNotNull(jsonObject.get("name"), "name field is missing");
            assertTrue(jsonObject.get("name").isJsonPrimitive() && jsonObject.get("name").getAsJsonPrimitive().isString(), "name field is not a string");

            assertNotNull(jsonObject.get("group"), "group field is missing");
            assertTrue(jsonObject.get("group").isJsonPrimitive() && jsonObject.get("group").getAsJsonPrimitive().isString(), "group field is not a string");

            assertNotNull(jsonObject.get("created"), "created field is missing");
            assertTrue(jsonObject.get("created").isJsonPrimitive() && jsonObject.get("created").getAsJsonPrimitive().isString(), "created field is not a string");

            assertNotNull(jsonObject.get("ok"), "ok field is missing");
            assertTrue(jsonObject.get("ok").isJsonPrimitive() && jsonObject.get("ok").getAsJsonPrimitive().isBoolean(), "ok field is not a boolean");
        }
    }

    @Test
    void restApiReturnsCorrectJsonWhenCheckingEmail() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(blacklistUrlCheckEmail))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected status code 200");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());
        assertTrue(jsonResponse.get("ok").asBoolean(), "Expected email to be not blacklisted");
    }

}
