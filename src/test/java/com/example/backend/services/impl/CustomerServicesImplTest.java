package com.example.backend.services.impl;

import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class CustomerServicesImplTest {
    Customer c;
    List<Booking> b;
    MiniCustomerDto mc;
    DetailedCustomerDto dc;
    @Mock
    private CustomerRepo mockCustomerRepo;

    @Mock
    private BookingRepo mockBookingRepo;

    @InjectMocks
    private CustomerServicesImpl cs;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        c = new Customer(1L, "Daniel", "Isaksson",
                "Daniel@hej.com", "0722055577");
        b = Arrays.asList(new Booking(1L, LocalDate.of(2022, 10, 10),
                        LocalDate.of(2022, 10, 12), 1,  new Room(1L, 3), c));
        c.setBookings(b);

        dc = cs.customerToDetailedCustomerDto(c);
        mc = cs.customerToMiniCustomerDto(c);
        System.out.println(dc.getMiniBookingDto().get(0).getId());

        when(mockCustomerRepo.findById(1L)).thenReturn(Optional.of(c));
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b.get(0)));
    }

    @Test
    void customerToDetailedCustomerDto() {
        Assertions.assertEquals(c.getId(), dc.getId());
        Assertions.assertEquals(c.getFirstName(), dc.getFirstName());
        Assertions.assertEquals(c.getLastName(), dc.getLastName());
        Assertions.assertEquals(c.getEmail(), dc.getEmail());
        Assertions.assertEquals(c.getPhoneNumber(), dc.getPhoneNumber());
        Assertions.assertEquals(1, dc.getMiniBookingDto().size());
    }

    @Test
    void customerToMiniCustomerDto() {
        Assertions.assertEquals(c.getId(), mc.getId());
        Assertions.assertEquals(c.getFirstName(), mc.getFirstName());
        Assertions.assertEquals(c.getLastName(), mc.getLastName());
        Assertions.assertEquals(c.getEmail(), mc.getEmail());
        Assertions.assertEquals(c.getPhoneNumber(), mc.getPhoneNumber());
    }

    @Test
    void miniCustomerDtoToCustomer() {
        Customer c2 = cs.miniCustomerDtoToCustomer(mc);

        Assertions.assertEquals(c2.getId(), mc.getId());
        Assertions.assertEquals(c2.getFirstName(), mc.getFirstName());
        Assertions.assertEquals(c2.getLastName(), mc.getLastName());
        Assertions.assertEquals(c2.getEmail(), mc.getEmail());
        Assertions.assertEquals(c2.getPhoneNumber(), mc.getPhoneNumber());
    }

    @Test
    void detailedCustomerDtoToCustomer() {
        Customer c2 = cs.detailedCustomerDtoToCustomer(dc);

        System.out.println(c2.getBookings().get(0).getId());
        System.out.println(dc.getMiniBookingDto().get(0).getId());

        Assertions.assertEquals(c2.getId(), dc.getId());
        Assertions.assertEquals(c2.getFirstName(), dc.getFirstName());
        Assertions.assertEquals(c2.getLastName(), dc.getLastName());
        Assertions.assertEquals(c2.getEmail(), dc.getEmail());
        Assertions.assertEquals(c2.getPhoneNumber(), dc.getPhoneNumber());
        Assertions.assertEquals(c2.getBookings().get(0).getId(), dc.getMiniBookingDto().get(0).getId());
    }

}