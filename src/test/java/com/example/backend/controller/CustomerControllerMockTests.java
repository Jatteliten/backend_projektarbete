package com.example.backend.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerMockTests {
    @Autowired
    private CustomerController cc;

    @Test
    public void contextLoads() {assertThat(cc).isNotNull();}

}
