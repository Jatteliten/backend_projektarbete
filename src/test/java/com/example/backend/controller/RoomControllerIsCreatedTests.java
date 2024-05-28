package com.example.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RoomControllerIsCreatedTests {
    @Autowired
    private RoomController roomController;

    @Test
    public void contextLoad() {
        assertThat(roomController).isNotNull();
    }
}
