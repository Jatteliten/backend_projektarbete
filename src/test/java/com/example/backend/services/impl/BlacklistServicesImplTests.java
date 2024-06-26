package com.example.backend.services.impl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.backend.configuration.IntegrationProperties;
import com.example.backend.model.Blacklist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BlacklistServicesImplTests {
    private String jsonContent;

    @Autowired
    private IntegrationProperties integrationProperties;

    private final HttpClient mockHttpClient = mock(HttpClient.class);

    private BlacklistServicesImpl blacklistServices;

    private HttpResponse<String> mockResponse;

    @Value("${integrations.blacklist.testUrl}")
    private String blacklistUrl;

    @Value("${integrations.blacklist.testUrlEmailCheck}")
    private String blacklistUrlCheckEmail;

    @Value("${integrations.blacklist.testData}")
    private String testData;

    @BeforeEach
    void setUp() {
        blacklistServices = new BlacklistServicesImpl(mockHttpClient, integrationProperties);
        MockitoAnnotations.initMocks(this);
        mockResponse = Mockito.mock(HttpResponse.class);

        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(testData)));
        } catch (IOException e) {
            System.err.println("Error reading test data file: " + e.getMessage());
            fail("Unable to read test data file: " + e.getMessage());
        }
    }

    @Test
    void fetchBlacklistReturnsExpectedData() throws IOException, InterruptedException {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);

        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Blacklist> blacklist = blacklistServices.fetchBlacklist();
        assertEquals(5, blacklist.size());

        assertEquals(1L, blacklist.get(0).getId());
        assertEquals("John Doe", blacklist.get(0).getName());
        assertEquals("john@example.com", blacklist.get(0).getEmail());
        assertTrue(blacklist.get(0).isOk());

        assertEquals(2L, blacklist.get(1).getId());
        assertEquals("Jane Smith", blacklist.get(1).getName());
        assertEquals("jane@example.com", blacklist.get(1).getEmail());
        assertFalse(blacklist.get(1).isOk());
    }

    @Test
    void fetchBlacklistReturnsEmptyList() throws IOException, InterruptedException {
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

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = blacklistServices.addPersonToBlacklist(email, name);

        verify(mockHttpClient).send(argThat(req -> {
            try {
                return req.uri().equals(new URI(integrationProperties.getBlacklist().getUrl()))
                        && req.headers().firstValue("Content-Type").get().equals("application/json")
                        && req.bodyPublisher().isPresent()
                        && req.bodyPublisher().get().contentLength() > 0;
            } catch (Exception e) {
                return false;
            }
        }), any(HttpResponse.BodyHandler.class));

        assertEquals("Person added to blacklist successfully.", result);
    }

    @Test
    void addPersonToBlacklistStatus500() throws IOException, InterruptedException {
        String email = "test@example.com";
        String name = "John Doe";

        when(mockResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = blacklistServices.addPersonToBlacklist(email, name);

        verify(mockHttpClient).send(argThat(request ->
                        request.uri().equals(URI.create(integrationProperties.getBlacklist().getUrl())) &&
                                request.method().equals("POST") &&
                                request.headers().firstValue("Content-Type").orElse("").equals("application/json") &&
                                request.bodyPublisher().isPresent() &&
                                request.bodyPublisher().get().contentLength() > 0
                ),
                any(HttpResponse.BodyHandler.class));

        assertEquals("Error when adding email to blacklist: 500", result);
    }

    @Test
    void addPersonAlreadyOnBlacklistStatus400() throws IOException, InterruptedException{
        String email = "test@example.com";
        String name = "John Doe";

        when(mockResponse.statusCode()).thenReturn(400);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = blacklistServices.addPersonToBlacklist(email, name);

        verify(mockHttpClient).send(argThat(request ->
                        request.uri().equals(URI.create(integrationProperties.getBlacklist().getUrl())) &&
                                request.method().equals("POST") &&
                                request.headers().firstValue("Content-Type").orElse("").equals("application/json") &&
                                request.bodyPublisher().isPresent() &&
                                request.bodyPublisher().get().contentLength() > 0
                ),
                any(HttpResponse.BodyHandler.class));

        assertEquals("Email already on blacklist.", result);
    }


    @Test
    void updateBlacklistedPersonSuccess() throws IOException, InterruptedException {
        String email = "test@example.com";
        String newName = "New Name";
        boolean newOkStatus = true;

        when(mockResponse.statusCode()).thenReturn(204);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String resultMessage = blacklistServices.updateBlacklistedPerson(email, newName, newOkStatus);

        verify(mockHttpClient).send(argThat(req -> {
            try {
                return req.uri().equals(new URI(integrationProperties.getBlacklist().getUrl() + "/" + email))
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

        when(mockResponse.statusCode()).thenReturn(400);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String resultMessage = blacklistServices.updateBlacklistedPerson(email, newName, newOkStatus);

        verify(mockHttpClient).send(argThat(req -> {
            try {
                return req.uri().equals(new URI(integrationProperties.getBlacklist().getUrl() + "/" + email))
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
    void filterBlacklistReturnsMatchingEntries() throws IOException, InterruptedException {

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Blacklist> nameMatches = blacklistServices.filterBlacklist("John Doe");
        assertEquals(1, nameMatches.size());
        assertEquals("John Doe", nameMatches.get(0).getName());

        List<Blacklist> emailMatches = blacklistServices.filterBlacklist("jane@example.com");
        assertEquals(1, emailMatches.size());
        assertEquals("jane@example.com", emailMatches.get(0).getEmail());

        List<Blacklist> idMatches = blacklistServices.filterBlacklist("2");
        assertEquals(1, idMatches.size());
        assertEquals("2", idMatches.get(0).getId().toString());

    }

    @Test
    void filterBlacklistReturnsEmptyListForNonMatchingSearch() throws IOException, InterruptedException {

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        List<Blacklist> nonMatchingSearch = blacklistServices.filterBlacklist("NonExistentSearch");
        assertEquals(0, nonMatchingSearch.size());
    }

    @Test
    void findBlacklistObjByIdShouldReturnMatchingEntry() throws IOException, InterruptedException {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Blacklist match = blacklistServices.findBlacklistObjById(1L);
        assertEquals(1L, match.getId());
        assertEquals("John Doe", match.getName());
        assertEquals("john@example.com", match.getEmail());
        assertTrue(true, String.valueOf(match.isOk()));
    }

    @Test
    void findBlacklistObjByIdShouldReturnNoMatchingEntry() throws IOException, InterruptedException {
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonContent);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Blacklist match = blacklistServices.findBlacklistObjById(100L);
        assertNull(match);
    }


    @Test
    void validEmailShouldReturnTrue() {
        assertTrue(blacklistServices.validEmail("test@example.com"));
        assertTrue(blacklistServices.validEmail("user_name@example.com"));
        assertTrue(blacklistServices.validEmail("username@sub.domain.com"));
    }

    @Test
    void validEmailShouldReturnFalse() {
        assertFalse(blacklistServices.validEmail("plainaddress"));
        assertFalse(blacklistServices.validEmail("@missingusername.com"));
        assertFalse(blacklistServices.validEmail("username@.com"));
        assertFalse(blacklistServices.validEmail("username@.com."));
        assertFalse(blacklistServices.validEmail("username@domain..com"));
    }

    @Test
    void validNameShouldReturnTrue() {
        assertTrue(blacklistServices.validName("John Doe"));
        assertTrue(blacklistServices.validName("Émilie du Châtelet"));
        assertTrue(blacklistServices.validName("O'Connor"));
        assertTrue(blacklistServices.validName("Anne-Marie"));
        assertTrue(blacklistServices.validName("Łukasz Żółć"));
    }

    @Test
    void validNameShouldReturnFalse() {
        assertFalse(blacklistServices.validName("John123"));
        assertFalse(blacklistServices.validName("Anne@Marie"));
        assertFalse(blacklistServices.validName("Émilie#du#Châtelet"));
        assertFalse(blacklistServices.validName("John_Doe"));
    }

}
