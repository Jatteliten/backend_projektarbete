package com.example.backend.services;

import com.example.backend.model.Booking;

public interface DiscountServices {
    double calculateTotalPriceWithDiscounts(Booking booking);
    double calculateFullPrice(Booking booking);
    boolean checkSundayToMondayDiscount(Booking booking);
    double applySundayToMondayDiscount(Booking booking,double fullPrice);
    boolean checkMoreThanTwoNightsDiscount(Booking booking);
    double calculateMoreThanTwoNightsDiscount(double initialPriceToPay);
    long calculateAmountsOfNightsCustomerBookedWithinOneYear(Booking booking);
    boolean checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(Booking booking);
    double calculateMoreThanTenNightsDiscount(double initialPriceToPay);
    Long calculateAmountOfNightsBooked(Booking booking);
}
