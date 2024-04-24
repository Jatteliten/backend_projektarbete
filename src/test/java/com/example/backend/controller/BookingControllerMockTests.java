package com.example.backend.controller;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerMockTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingRepo mockRepo;

    @Autowired
    private BookingController bc;

    @BeforeEach
    private void init(){
        Booking b1 = new Booking(1L, LocalDate.of(2020,12,12),
                LocalDate.of(2020,12,15),1,
                new Room(1L, 2),
                new Customer(1L, "Daniel", "Isaksson",
                "hej@Daniel.com", "0722223344"));

        Booking b2 = new Booking(2L, LocalDate.of(2021,2,5),
                LocalDate.of(2021,2,7),0,
                new Room(2L, 2),
                new Customer(2L, "Gustaf", "Forsberg",
                        "hej@Gustaf.com", "0723346344"));

        Booking b3 = new Booking(3L, LocalDate.of(2022,1,1),
                LocalDate.of(2022,1,5),2,
                new Room(3L, 4),
                new Customer(3L, "Petter", "Tornberg",
                        "hej@Petter.com", "0722445344"));

        when(mockRepo.findById(1L)).thenReturn(Optional.of(b1));
        when(mockRepo.findById(2L)).thenReturn(Optional.of(b2));
        when(mockRepo.findById(3L)).thenReturn(Optional.of(b3));
        when(mockRepo.findAll()).thenReturn(Arrays.asList(b1, b2, b3));
    }

    @Test
    public void contextLoads() {assertThat(bc).isNotNull();}

    @Test
    void getBookingById() throws Exception {
        this.mvc.perform(get("/booking/getBooking/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1}"));
    }



}
