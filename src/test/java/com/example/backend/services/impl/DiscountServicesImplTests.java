package com.example.backend.services.impl;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.services.DiscountServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DiscountServicesImplTests {


    private final BookingRepo bookingRepo = mock(BookingRepo.class);

    DiscountServices sut;


    private Booking twoNightsBooking;
    private Booking fridayToMondayBooking;
    private Booking oneNightBooking;

    private Booking tenNightBooking;
    private Booking sundayToMondayBooking;


    @BeforeEach
    void setUp() {
        sut = new DiscountServicesImpl(bookingRepo);



        Customer c1 = new Customer(1,"Martin",
                "Harrysson","Martin@hej.se","0704455667",null);

        Room r1 = Room.builder().id(1L).size(1).pricePerNight(100).build();

        twoNightsBooking = new Booking(1L, LocalDate.of(2024,5,1),
                LocalDate.of(2024,5,3),0,r1,c1);

        fridayToMondayBooking = new Booking(1L, LocalDate.of(2024,5,3),
                LocalDate.of(2024,5,6),0,r1,c1);
        oneNightBooking = new Booking(1L, LocalDate.of(2024,5,3),
                LocalDate.of(2024,5,4),0,r1,c1);
        tenNightBooking = new Booking(1L, LocalDate.of(2024,5,3),
                LocalDate.of(2024,5,13),0,r1,c1);
        sundayToMondayBooking = new Booking(1L, LocalDate.of(2024,5,5),
                LocalDate.of(2024, 5,6),0,r1,c1);
    }



    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnFullPrice() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(oneNightBooking);
        //assert
        assertEquals(100,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithTwoNightsDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(twoNightsBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(twoNightsBooking);
        //assert
        assertEquals(199,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithSundayToMondayDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(sundayToMondayBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(sundayToMondayBooking);
        //assert
        assertEquals(98,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithSundayToMondayAndTwoNightDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(twoNightsBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(fridayToMondayBooking);
        //assert
        assertEquals(296.51,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithTwoNightAndMoreThanTenDaysDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,tenNightBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(twoNightsBooking);
        //assert
        assertEquals(195,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithSundayToMondayAndMoreThanTenDaysDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,tenNightBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(sundayToMondayBooking);
        //assert
        assertEquals(96.04,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithSundayToMondayAndTwoNightandMoreThanTenDaysDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,tenNightBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(fridayToMondayBooking);
        //assert
        assertEquals(290.55,actualPrice);
    }
    @Test
    void calculateTotalPriceWithDiscountsTestShouldReturnPriceWithMoreThanTenDaysDiscount() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,tenNightBooking));

        //apply
        double actualPrice = sut.calculateTotalPriceWithDiscounts(oneNightBooking);
        //assert
        assertEquals(98,actualPrice);
    }


    @Test
    void calculateFullPriceForTwoNightsTestShouldReturn200() {
        assertEquals(200,sut.calculateFullPrice(twoNightsBooking));
    }

    @Test
    void checkSundayToMondayDiscountTestShouldReturnTrue() {
        //booking with dates that have night between sunday/monday
        assertTrue(sut.checkSundayToMondayDiscount(fridayToMondayBooking));
    }
    @Test
    void checkSundayToMondayDiscountTestShouldReturnFalse() {
        //booking with dates that don't have night between sunday/monday
        assertFalse(sut.checkSundayToMondayDiscount(twoNightsBooking));
    }

    @Test
    void applySundayToMondayDiscountTestShouldReturnDiscountedPrice() {
        assertEquals(298,sut.applySundayToMondayDiscount(fridayToMondayBooking,300));
    }
    @Test
    void applySundayToMondayDiscountTestShouldNotReturnDiscountedPrice() {
        assertEquals(300,sut.applySundayToMondayDiscount(twoNightsBooking,300));
    }

    @Test
    void checkMoreThanTwoNightsDiscountTestShouldReturnTrue() {
        assertTrue(sut.checkMoreThanTwoNightsDiscount(twoNightsBooking));
    }
    @Test
    void checkMoreThanTwoNightsDiscountTestShouldReturnFalse() {
        assertFalse(sut.checkMoreThanTwoNightsDiscount(oneNightBooking));
    }

    @Test
    void calculateMoreThanTwoNightsDiscounTestShouldReturnDiscountFromInitialPrice() {
        assertEquals(5,sut.calculateMoreThanTwoNightsDiscount(1000));
    }

    @Test
    void calculateAmountsOfNightsCustomerBookedWithinOneYearShouldReturn3nights() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,twoNightsBooking));

        //apply
        long actualValue = sut.calculateAmountsOfNightsCustomerBookedWithinOneYear(twoNightsBooking);

        //assert
        assertEquals(3,actualValue);
    }


    @Test
    void checkIfCustomerHaveMoreThanTenBookingNightsWithinAYearShouldReturnTrue() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(tenNightBooking,twoNightsBooking));

        //apply
        boolean actualValue = sut.checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(oneNightBooking);

        //assert
        assertTrue(actualValue);

    }
    @Test
    void checkIfCustomerHaveMoreThanTenBookingNightsWithinAYearShouldReturnFalse() {
        //assemble
        when(bookingRepo.findByCustomerIdAndStartDateAfter(eq(1L),any())).thenReturn(List.of(oneNightBooking,twoNightsBooking));

        //apply
        boolean actualValue = sut.checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(oneNightBooking);

        //assert
        assertFalse(actualValue);
    }

    @Test
    void calculateMoreThanTenNightsDiscountTestShouldReturn2PercentOfInitialPrice() {
        assertEquals(2,sut.calculateMoreThanTenNightsDiscount(100));
    }

    @Test
    void calculateAmountOfNightsBookedTestShouldReturn2Nights() {
        assertEquals(2,sut.calculateAmountOfNightsBooked(twoNightsBooking));
    }
}