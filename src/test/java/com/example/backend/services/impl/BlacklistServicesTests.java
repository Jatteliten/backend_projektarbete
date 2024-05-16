package com.example.backend.services.impl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;;

import com.example.backend.model.Blacklist;
import com.example.backend.services.BlacklistServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class BlacklistServicesTests {

    private BlacklistServicesImpl blacklistServices;

    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private HttpResponse mockHttpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        blacklistServices = new BlacklistServicesImpl(mockHttpClient);
    }

    @Test
    void fetchBlacklistSuccess() throws IOException, InterruptedException {
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


}
