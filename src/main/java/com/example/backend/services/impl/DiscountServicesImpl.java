package com.example.backend.services.impl;

import com.example.backend.model.Booking;
import com.example.backend.services.DiscountServices;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DiscountServicesImpl implements DiscountServices {
    private static final double moreThanTwoDaysDiscount = 0.995;
    private static final double sundayToMondayDiscount = 0.02;

    @Override
    public double calculateTotalPriceWithAllDiscounts(Booking booking) {

        final double fullPrice = calculateFullPrice(booking);

        final double priceWithSundayToMondayDiscount =
                fullPrice - calculateSundayToMondayDiscount(booking);

        final double priceWithMoreThanTwoNightsDiscount =
                priceWithSundayToMondayDiscount * checkMoreThanTwoNightsDiscount(booking);
        // lägg till VG del


        return priceWithMoreThanTwoNightsDiscount;
    }

    @Override
    public double calculateFullPrice(Booking booking) {
        Long amountOfNightsBooked = ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate());
        return amountOfNightsBooked * booking.getRoom().getPricePerNight();
    }

    @Override
    public double calculateSundayToMondayDiscount(Booking booking) {
        //Returns the discounted amount to be subtracted from full price
        LocalDate startDate = booking.getStartDate();
        int amountOfNightsSundayToMonday = 0;
        double discount = 0;

        while (startDate.isBefore(booking.getEndDate())){
            if (startDate.getDayOfWeek().equals(DayOfWeek.MONDAY) &&
                    startDate.plusDays(1).getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                amountOfNightsSundayToMonday ++;
                discount += booking.getRoom().getPricePerNight()*sundayToMondayDiscount;
            }
            startDate = startDate.plusDays(1);
        }

        return discount;
    }

    @Override
    public double checkMoreThanTwoNightsDiscount(Booking booking) {
        // returns a value to multiply a price with to calculate new price with discount
        if (ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate()) < 2){
            return 1;
        } else
            return moreThanTwoDaysDiscount;
    }
}
