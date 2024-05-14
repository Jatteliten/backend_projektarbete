package com.example.backend.services;

import com.example.backend.model.Booking;

public interface DiscountServices {
    double calculateTotalPriceWithAllDiscounts(Booking booking);
    double calculateFullPrice(Booking booking);
    double calculateSundayToMondayDiscount(Booking booking,double fullPrice);
    double calculateMoreThanTwoNightsDiscount(Booking booking, double initialPriceToPay);
    double calculateIfCustomerBookedMoreThan10NightsLastYearDiscount(Booking booking, double initialPriceToPay);
    Long calculateAmountOfNightsBooked(Booking booking);
}
