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
    private static final String BLACKLIST_API_URL = "https://javabl.systementor.se/api/asmadali/blacklist";

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
        assertTrue(blacklist.get(0).isOk());

        assertEquals(30L, blacklist.get(1).getId());
        assertEquals("Martin Harrysson", blacklist.get(1).getName());
        assertEquals("martin@gmail.com", blacklist.get(1).getEmail());
        assertFalse(blacklist.get(1).isOk());
    }

    @Test
    void fetchBlacklistReturnsEmptyList() throws IOException, InterruptedException {
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/blacklist_data.json")));
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn("");

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Blacklist> blacklist = blacklistServices.fetchBlacklist();
        assertEquals(0, blacklist.size());
    }

    @Test
    void isBlacklistedReturnsTrueWhenOkIsFalse() throws IOException, InterruptedException {
        String email = "test@example.com";
        String jsonResponse = "{\"ok\": false}";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        boolean isBlacklistedShouldReturnTrue = blacklistServices.isBlacklisted(email);
        assertTrue(isBlacklistedShouldReturnTrue);
    }

    @Test
    void isBlacklistedReturnsFalse() throws IOException, InterruptedException {
        String email = "test@example.com";
        String jsonResponse = "{\"ok\": true}";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        boolean isBlacklistedShouldReturnFalse = blacklistServices.isBlacklisted(email);
        assertFalse(isBlacklistedShouldReturnFalse);
    }

    @Test
    void isBlacklistedThrowsExceptionWhenStatusCodeIsOtherThan200() throws IOException, InterruptedException {
        String email = "test@example.com";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            blacklistServices.isBlacklisted(email);
        });

        assertTrue(exception.getMessage().contains("Failed to check blacklist status, status code: 500"));
    }

    @Test
    void isBlacklistedThrowsExceptionOnException() throws IOException, InterruptedException {
        String email = "test@example.com";
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            blacklistServices.isBlacklisted(email);
        });

        assertTrue(exception.getMessage().contains("Failed to check blacklist status: Network error"));
    }

    @Test
    void addPersonToBlacklistSuccess() throws IOException, InterruptedException {
        String email = "test@example.com";
        String name = "Test Person";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        //Returnar API:et någon body vid success/fail? Isåfall kanske testa det?
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = blacklistServices.addPersonToBlacklist(email, name);

       verify(mockHttpClient).send(argThat(req -> {
           try {
               return req.uri().equals(new URI(BLACKLIST_API_URL))
               && req.headers().firstValue("Content-Type").get().equals("application/json")
               && req.bodyPublisher().isPresent()
               && req.bodyPublisher().get().contentLength() > 0;
           } catch (Exception e) {
               return false;
           }
       }), any(HttpResponse.BodyHandler.class));

       assertEquals("Mail added to blacklist", result);
    }

    @Test
    void addPersonToBlacklistFail() throws IOException, InterruptedException {
        String email = "test@example.com";
        String name = "John Doe";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = blacklistServices.addPersonToBlacklist(email, name);

        verify(mockHttpClient).send(argThat(request ->
                        request.uri().equals(URI.create(BLACKLIST_API_URL)) &&
                                request.method().equals("POST") &&
                                request.headers().firstValue("Content-Type").orElse("").equals("application/json") &&
                                request.bodyPublisher().isPresent() &&
                                request.bodyPublisher().get().contentLength() > 0
                ),
                any(HttpResponse.BodyHandler.class));

        assertEquals("Error when adding email to blacklist: 500", result);
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
                return req.uri().equals(new URI(BLACKLIST_API_URL + email))
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
        String email = "test@example.com";
        String newName = "New Name";
        boolean newOkStatus = true;

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(400);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String resultMessage = blacklistServices.updateBlacklistedPerson(email, newName, newOkStatus);

        verify(mockHttpClient).send(argThat(req -> {
            try {
                return req.uri().equals(new URI(BLACKLIST_API_URL + email))
                        && req.method().equals("PUT")
                        && req.headers().firstValue("Content-Type").get().equals("application/json")
                        && req.bodyPublisher().isPresent()
                        && req.bodyPublisher().get().contentLength() > 0;
            } catch (Exception e) {
                return false;
            }
        }), any(HttpResponse.BodyHandler.class));

        assertEquals("Error while updating blacklisted person.", resultMessage);
    }

    @Test
    void filterBlacklistReturnsExpectedData() throws IOException, InterruptedException {

    }

    @Test
    void filterBlacklistReturnsEmptyList() throws IOException, InterruptedException {

    }
}
