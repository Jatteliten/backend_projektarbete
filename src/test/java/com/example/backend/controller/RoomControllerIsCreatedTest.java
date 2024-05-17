package com.example.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoomControllerIsCreatedTest {
    @Autowired
    private RoomController roomController;

    @Test
    public void contextLoad() {
        assertThat(roomController).isNotNull();
    }
}