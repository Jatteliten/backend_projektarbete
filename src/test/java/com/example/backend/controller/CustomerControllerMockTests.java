package com.example.backend.controller;

import com.example.backend.model.Customer;
import com.example.backend.repos.CustomerRepo;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerMockTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CustomerRepo mockRepo;
    @Autowired
    private CustomerController cc;

    @BeforeEach
    private void init(){
        Customer c1 = new Customer(1L, "Daniel", "Isaksson",
                "hej@Daniel.com", "0722223344");
        Customer c2 = new Customer(2L, "Gustaf", "Forsberg",
                "hej@Gustaf.com", "0722343344");
        Customer c3 = new Customer(3L, "Petter", "Tornberg",
                "hej@Petter.com", "0722543344");

        when(mockRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(mockRepo.findById(2L)).thenReturn(Optional.of(c2));
        when(mockRepo.findById(3L)).thenReturn(Optional.of(c3));
        when(mockRepo.findAll()).thenReturn(Arrays.asList(c1, c2, c3));
    }


    @Test
    public void contextLoads() {assertThat(cc).isNotNull();}

    @Test
    void getCustomerById() throws Exception {
        this.mvc.perform(get("/customers/getCustomer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }
}
