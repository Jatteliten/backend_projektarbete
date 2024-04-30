package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class BookingServicesImplTest {

    Customer c;
    Booking b;
    Room r;
    List<Booking> bList;
    MiniBookingDto mb;
    DetailedBookingDto db;
    @Mock
    private CustomerRepo mockCustomerRepo;

    @Mock
    private BookingRepo mockBookingRepo;

    @Mock
    private RoomRepo mockRoomRepo;

    @InjectMocks
    private BookingServicesImpl bs;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        c = new Customer(1L, "Daniel", "Isaksson",
                "Daniel@hej.com", "0722055577");
        r = new Room(1L, 3);
        b = new Booking(1L, LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 10, 12), 1, r, c);
        bList = Arrays.asList(b);
        c.setBookings(bList);

        db = bs.bookingToDetailedBookingDto(b);
        mb = bs.bookingToMiniBookingDto(b);

        when(mockCustomerRepo.findById(1L)).thenReturn(Optional.of(c));
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b));
        when(mockRoomRepo.findById(1L)).thenReturn(Optional.of(r));
    }

    @Test
    void bookingToDetailedBookingDto() {
        Assertions.assertEquals(b.getId(), db.getId());
        Assertions.assertEquals(b.getRoom().getSize(), db.getMiniRoomDto().getSize());
        Assertions.assertEquals(b.getExtraBeds(), db.getExtraBeds());
        Assertions.assertEquals(b.getCustomer().getId(), db.getMiniCustomerDto().getId());
        Assertions.assertEquals(b.getRoom().getId(), db.getMiniRoomDto().getId());
        Assertions.assertEquals(b.getStartDate().getYear(), db.getStartDate().getYear());
        Assertions.assertEquals(b.getStartDate().getMonth(), db.getStartDate().getMonth());
        Assertions.assertEquals(b.getStartDate().getDayOfMonth(), db.getStartDate().getDayOfMonth());
    }

    @Test
    void bookingToMiniBookingDto() {
        Assertions.assertEquals(b.getId(), mb.getId());
        Assertions.assertEquals(b.getCustomer().getId(), mb.getMiniCustomerDto().getId());
        Assertions.assertEquals(b.getRoom().getId(), mb.getMiniRoomDto().getId());
        Assertions.assertEquals(b.getStartDate().getYear(), mb.getStartDate().getYear());
        Assertions.assertEquals(b.getStartDate().getMonth(), mb.getStartDate().getMonth());
        Assertions.assertEquals(b.getStartDate().getDayOfMonth(), mb.getStartDate().getDayOfMonth());
    }

    @Test
    void detailedBookingDtoToBooking() {
        Booking b2 = bs.detailedBookingDtoToBooking(db);

        Assertions.assertEquals(b2.getId(), db.getId());
        Assertions.assertEquals(b2.getRoom().getSize(), db.getMiniRoomDto().getSize());
        Assertions.assertEquals(b2.getExtraBeds(), db.getExtraBeds());
        Assertions.assertEquals(b2.getCustomer().getId(), db.getMiniCustomerDto().getId());
        Assertions.assertEquals(b2.getRoom().getId(), db.getMiniRoomDto().getId());
        Assertions.assertEquals(b2.getStartDate().getYear(), db.getStartDate().getYear());
        Assertions.assertEquals(b2.getStartDate().getMonth(), db.getStartDate().getMonth());
        Assertions.assertEquals(b2.getStartDate().getDayOfMonth(), db.getStartDate().getDayOfMonth());
    }

}