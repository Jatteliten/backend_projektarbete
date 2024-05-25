package com.example.backend;

import com.example.backend.configuration.IntegrationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

//Funkar inte :(
@TestConfiguration
@Profile("test")
public class TestProfileConfiguration {

    @Bean
    public IntegrationProperties integrationProperties() {
        return new IntegrationProperties();
    }
}
