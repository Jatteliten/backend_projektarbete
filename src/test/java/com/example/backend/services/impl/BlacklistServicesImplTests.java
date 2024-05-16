package com.example.backend.services.impl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
;

import com.example.backend.model.Blacklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BlacklistServicesImplTests {

    private BlacklistServicesImpl blacklistServices;

    @Mock
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        blacklistServices = new BlacklistServicesImpl(mockHttpClient);
    }

    @Test
    void fetchBlacklistReturnsExpectedData() throws IOException, InterruptedException {
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/blacklist_data.json")));
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Blacklist> blacklist = blacklistServices.fetchBlacklist();
        assertEquals(5, blacklist.size());

        assertEquals(29L, blacklist.get(0).getId());
        assertEquals("Lise Martinsen", blacklist.get(0).getName());
        assertEquals("lise@gmail.com", blacklist.get(0).getEmail());

        assertEquals(30L, blacklist.get(1).getId());
        assertEquals("Martin Harrysson", blacklist.get(1).getName());
        assertEquals("martin@gmail.com", blacklist.get(1).getEmail());

        //assertEquals(false, blacklist.get(1).getOk());    Varför funkar inte detta? Varför finns ingen getter för ok?

    }

    @Test
    void isBlacklistedReturnsTrue() throws IOException, InterruptedException {

        String jsonResponse = "{\"ok\": false}";
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String email = "martin@gmail.com";
        boolean isBlacklistedShouldReturnTrue = blacklistServices.isBlacklisted(email);
        assertTrue(isBlacklistedShouldReturnTrue);
    }

    @Test
    void isBlacklistedReturnsFalse() throws IOException, InterruptedException {

        String jsonResponse = "{\"ok\": true}";
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String email = "lise@gmail.com";
        boolean isBlacklistedShouldReturnFalse = blacklistServices.isBlacklisted(email);
        assertFalse(isBlacklistedShouldReturnFalse);
    }

    @Test
    void addPersonToBlacklistSuccess() throws IOException, InterruptedException {
        String email = "test@example.com";
        String name = "Test Person";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        blacklistServices.addPersonToBlacklist(email, name);

       verify(mockHttpClient).send(argThat(req -> {
           try {
               return req.uri().equals(new URI("https://javabl.systementor.se/api/asmadali/blacklist"))
               && req.headers().firstValue("Content-Type").get().equals("application/json")
               && req.bodyPublisher().isPresent()
               && req.bodyPublisher().get().contentLength() > 0;
           } catch (Exception e) {
               return false;
           }
       }), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void updateBlacklistedPersonSuccess() throws IOException, InterruptedException {
        String email = "test@example.com";
        String newName = "New Name";
        boolean newOkStatus = true;

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(204);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String resultMessage = blacklistServices.updateBlacklistedPerson(email, newName, newOkStatus);

        verify(mockHttpClient).send(argThat(req -> {
            try {
                return req.uri().equals(new URI("https://javabl.systementor.se/api/asmadali/blacklist/" + email))
                        && req.method().equals("PUT")
                        && req.headers().firstValue("Content-Type").get().equals("application/json")
                        && req.bodyPublisher().isPresent()
                        && req.bodyPublisher().get().contentLength() > 0;
            } catch (Exception e) {
                return false;
            }
        }), any(HttpResponse.BodyHandler.class));

        assertEquals("Blacklisted person updated successfully.", resultMessage);

    }

    @Test
    void updateBlacklistedPersonFail() throws IOException, InterruptedException {

    }
}
