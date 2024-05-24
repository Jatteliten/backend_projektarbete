package com.example.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient() {
        System.out.println("INNE I HTTPCLIENT BEAN!!!!");
        return HttpClient.newHttpClient();
    }
}
