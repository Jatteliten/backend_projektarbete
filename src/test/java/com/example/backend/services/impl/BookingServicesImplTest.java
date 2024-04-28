package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.AddBookingView;
import com.example.backend.controller.CustomerController;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class BookingServicesImplTest {

    @Autowired
    private BookingServicesImpl b;


    @Test
    void validateTest() {
        AddBookingView value1 = new AddBookingView(LocalDate.now().minusDays(1),LocalDate.now().plusDays(3));
        AddBookingView value2 = new AddBookingView(LocalDate.now().plusDays(3),LocalDate.now());
        AddBookingView value3 = new AddBookingView(LocalDate.now().minusDays(1),LocalDate.now().minusDays(3));
        AddBookingView value4 = new AddBookingView(LocalDate.now(),LocalDate.now().plusDays(3));
        List<String> expectedValue1 = List.of("Cant book room before todays date");
        List<String> expectedValue2 = List.of("Cant book room with start date before end date");
        List<String> expectedValue3 = List.of("Cant book room before todays date","Cant book room with start date before end date");
        List<String> expectedValue4 = List.of();
        List<String> actualValue1 = b.validate(value1);
        List<String> actualValue2 = b.validate(value2);
        List<String> actualValue3 = b.validate(value3);
        List<String> actualValue4 = b.validate(value4);
        assertEquals(expectedValue1,actualValue1);
        assertEquals(expectedValue2,actualValue2);
        assertEquals(expectedValue3,actualValue3);
        assertEquals(expectedValue4,actualValue4);

    }

    @Test
    void checkNotAvailableTest() {
        // check if date is already booked
        Booking b1 = new Booking(LocalDate.now().plusDays(1),LocalDate.now().plusDays(3));
        Boolean actualValue = b.checkNotAvailable(b1,LocalDate.now().plusDays(1),LocalDate.now().plusDays(3));
        assertTrue(actualValue);
        // check if date is available
        actualValue = b.checkNotAvailable(b1,LocalDate.now().plusDays(4),LocalDate.now().plusDays(6));
        assertFalse(actualValue);
    }

    @Test
    void calculateExtraBedsTest() {
        Room room = new Room(3);
        int expectedValue = 1;
        assertEquals(expectedValue,b.calculateExtraBeds(room));

    }
}