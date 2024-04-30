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

    Customer c1;
    Customer c2;
    Booking b1;
    Booking b2;
    Room r1;
    Room r2;
    List<Booking> b1List;
    List<Booking> b2List;
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
        c1 = new Customer(1L, "Daniel", "Isaksson",
                "Daniel@hej.com", "0722055577");
        c2 = new Customer(2L, "Sarah", "Wrengler",
                "Sarah@hej.com", "0733023322");
        r1 = new Room(1L, 3);
        r2 = new Room(2L, 4);
        b1 = new Booking(1L, LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 10, 12), 1, r1, c1);
        b2 = new Booking(2L, LocalDate.of(2023, 12, 12),
                LocalDate.of(2023, 12, 20), 2, r2, c2);
        b1List = Arrays.asList(b1);
        b2List = Arrays.asList(b2);

        c1.setBookings(b1List);
        c2.setBookings(b2List);

        db = bs.bookingToDetailedBookingDto(b1);
        mb = bs.bookingToMiniBookingDto(b1);

        when(mockCustomerRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b1));
        when(mockRoomRepo.findById(1L)).thenReturn(Optional.of(r1));
    }

    @Test
    void bookingToDetailedBookingDto() {
        Assertions.assertEquals(b1.getId(), db.getId());
        Assertions.assertEquals(b1.getRoom().getSize(), db.getMiniRoomDto().getSize());
        Assertions.assertEquals(b1.getExtraBeds(), db.getExtraBeds());
        Assertions.assertEquals(b1.getCustomer().getId(), db.getMiniCustomerDto().getId());
        Assertions.assertEquals(b1.getRoom().getId(), db.getMiniRoomDto().getId());
        Assertions.assertEquals(b1.getStartDate().getYear(), db.getStartDate().getYear());
        Assertions.assertEquals(b1.getStartDate().getMonth(), db.getStartDate().getMonth());
        Assertions.assertEquals(b1.getStartDate().getDayOfMonth(), db.getStartDate().getDayOfMonth());
    }

    @Test
    void bookingToMiniBookingDto() {
        Assertions.assertEquals(b1.getId(), mb.getId());
        Assertions.assertEquals(b1.getCustomer().getId(), mb.getMiniCustomerDto().getId());
        Assertions.assertEquals(b1.getRoom().getId(), mb.getMiniRoomDto().getId());
        Assertions.assertEquals(b1.getStartDate().getYear(), mb.getStartDate().getYear());
        Assertions.assertEquals(b1.getStartDate().getMonth(), mb.getStartDate().getMonth());
        Assertions.assertEquals(b1.getStartDate().getDayOfMonth(), mb.getStartDate().getDayOfMonth());
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

    @Test
    void getAllDetailedBookings() {
        List<Booking> bookings = Arrays.asList(b1, b2);

        when(mockBookingRepo.findAll()).thenReturn(bookings);

        List<DetailedBookingDto> dbList = bs.getAllDetailedBookings();
        Assertions.assertEquals(2, dbList.size());
        Assertions.assertEquals(1L, dbList.get(0).getId());
        Assertions.assertEquals(1, dbList.get(0).getExtraBeds());
        Assertions.assertEquals(2, dbList.get(1).getExtraBeds());
        Assertions.assertEquals(1L, dbList.get(0).getMiniCustomerDto().getId());
        Assertions.assertEquals(3, dbList.get(0).getMiniRoomDto().getSize());
        Assertions.assertEquals(2L, dbList.get(1).getMiniCustomerDto().getId());
        Assertions.assertEquals(4, dbList.get(1).getMiniRoomDto().getSize());
    }

    @Test
    void getAllMiniBookings() {
        List<Booking> bookings = Arrays.asList(b1, b2);

        when(mockBookingRepo.findAll()).thenReturn(bookings);
        List<MiniBookingDto> mbList = bs.getAllMiniBookings();


        Assertions.assertEquals(2, mbList.size());
        Assertions.assertEquals(1L, mbList.get(0).getId());
        Assertions.assertEquals(LocalDate.of(2022, 10, 10), mbList.get(0).getStartDate());
        Assertions.assertEquals(1L, mbList.get(0).getMiniCustomerDto().getId());
        Assertions.assertEquals(3, mbList.get(0).getMiniRoomDto().getSize());
        Assertions.assertEquals(2L, mbList.get(1).getMiniCustomerDto().getId());
        Assertions.assertEquals(4, mbList.get(1).getMiniRoomDto().getSize());
    }

    @Test
    void getDetailedBookingById() {
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b1));

        DetailedBookingDto db = bs.getDetailedBookingById(1L);

        Assertions.assertEquals(1L, db.getId());
        Assertions.assertEquals(1, db.getExtraBeds());
        Assertions.assertEquals(1L, db.getMiniCustomerDto().getId());
        Assertions.assertEquals(1L, db.getMiniRoomDto().getId());
    }

    @Test
    void getMiniBookingById() {
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b1));

        MiniBookingDto db = bs.getMiniBookingById(1L);

        Assertions.assertEquals(1L, db.getId());
        Assertions.assertEquals(1L, db.getMiniCustomerDto().getId());
        Assertions.assertEquals(1L, db.getMiniRoomDto().getId());
    }

}