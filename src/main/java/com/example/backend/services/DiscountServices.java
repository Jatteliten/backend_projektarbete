package com.example.backend.services;

import com.example.backend.model.Booking;

public interface DiscountServices {
    double calculateTotalPriceWithAllDiscounts(Booking booking);
    double calculateFullPrice(Booking booking);
    double calculateSundayToMondayDiscount(Booking booking);
    double checkMoreThanTwoNightsDiscount(Booking booking);
}
