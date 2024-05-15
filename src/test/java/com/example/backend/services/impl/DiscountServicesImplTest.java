package com.example.backend.services.impl;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.services.DiscountServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DiscountServicesImplTest {


    private BookingRepo bookingRepo = mock(BookingRepo.class);

    DiscountServices sut;



    private Customer c1;

    private Room r1;

    private Booking twoNightsBooking;
    private Booking fridayToMondayBooking;
    private Booking oneNightBooking;

    @BeforeEach
    void setUp() {
        sut = new DiscountServicesImpl(bookingRepo);


        c1 = new Customer(1,"Martin",
                "Harrysson","Martin@hej.se","0704455667",null);

        r1 = new Room(1l,1,100);

        twoNightsBooking = new Booking(1l, LocalDate.of(2024,05,01),
                LocalDate.of(2024,05,03),0,r1,c1);

        fridayToMondayBooking = new Booking(1l, LocalDate.of(2024,05,03),
                LocalDate.of(2024,05,06),0,r1,c1);
        oneNightBooking = new Booking(1l, LocalDate.of(2024,05,03),
                LocalDate.of(2024,05,04),0,r1,c1);
    }

    @Test
    void calculateTotalPriceWithAllDiscountsTest() {
    }

    @Test
    void calculateFullPriceForTwoNightsTest() {
        //booking for two nights
        assertEquals(200,sut.calculateFullPrice(twoNightsBooking));
    }

    @Test
    void checkSundayToMondayDiscountTest() {
        //booking with dates that have night between sunday/monday
        assertTrue(sut.checkSundayToMondayDiscount(fridayToMondayBooking));
        //booking with dates that don't have night between sunday/monday
        assertFalse(sut.checkSundayToMondayDiscount(twoNightsBooking));
    }

    @Test
    void applySundayToMondayDiscountTest() {
        assertEquals(200,sut.applySundayToMondayDiscount(twoNightsBooking,200));
        assertEquals(298,sut.applySundayToMondayDiscount(fridayToMondayBooking,300));
    }

    @Test
    void checkMoreThanTwoNightsDiscountTest() {
        assertFalse(sut.checkMoreThanTwoNightsDiscount(oneNightBooking));
        assertTrue(sut.checkMoreThanTwoNightsDiscount(twoNightsBooking));
    }

    @Test
    void calculateMoreThanTwoNightsDiscounTest() {
        assertEquals(5,sut.calculateMoreThanTwoNightsDiscount(1000));
    }

    @Test
    void calculateAmountsOfNightsCustomerBookedWithinOneYearTest() {
        //vg bookingRepoMock
    }

    @Test
    void checkIfCustomerHaveMoreThanTenBookingNightsWithinAYearTest() {
        //VG bookingMock
    }

    @Test
    void calculateMoreThanTenNightsDiscountTest() {
        assertEquals(2,sut.calculateMoreThanTenNightsDiscount(100));
    }

    @Test
    void calculateAmountOfNightsBookedTest() {
        assertEquals(2,sut.calculateAmountOfNightsBooked(twoNightsBooking));
        assertEquals(1,sut.calculateAmountOfNightsBooked(oneNightBooking));
    }
}