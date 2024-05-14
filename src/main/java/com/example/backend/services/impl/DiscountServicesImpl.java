package com.example.backend.services.impl;

import com.example.backend.model.Booking;
import com.example.backend.repos.BookingRepo;
import com.example.backend.services.DiscountServices;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DiscountServicesImpl implements DiscountServices {
    private static final double moreThanTwoDaysDiscount = 0.005;
    private static final double sundayToMondayDiscount = 0.02;
    private static final double moreThan10DaysBookedDiscount = 0.02;


    private final BookingRepo bookingRepo;

    DiscountServicesImpl(BookingRepo bookingRepo){
        this.bookingRepo = bookingRepo;
    }

    @Override
    public double calculateTotalPriceWithAllDiscounts(Booking booking) {

        final double fullPrice = calculateFullPrice(booking);

        //initial price with standard sunday to monday discount
        final double priceWithSundayToMondayDiscount = calculateSundayToMondayDiscount(booking,fullPrice);

        double totalDiscount =
                calculateIfCustomerBookedMoreThan10NightsLastYearDiscount(booking,priceWithSundayToMondayDiscount)+
                calculateMoreThanTwoNightsDiscount(booking,priceWithSundayToMondayDiscount);

        return priceWithSundayToMondayDiscount-totalDiscount;
    }

    @Override
    public double calculateFullPrice(Booking booking) {
        Long amountOfNightsBooked = ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate());
        return amountOfNightsBooked * booking.getRoom().getPricePerNight();
    }

    @Override
    public double calculateSundayToMondayDiscount(Booking booking,double fullPrice) {
        //returns the initial price to pay after standars sunday to monday discount
        LocalDate startDate = booking.getStartDate();
        double discount = 0;

        while (startDate.isBefore(booking.getEndDate())){
            if (startDate.getDayOfWeek().equals(DayOfWeek.MONDAY) &&
                    startDate.plusDays(1).getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                discount += booking.getRoom().getPricePerNight()*sundayToMondayDiscount;
            }
            startDate = startDate.plusDays(1);
        }

        return fullPrice - discount;
    }

    @Override
    public double calculateMoreThanTwoNightsDiscount(Booking booking, double initialPriceToPay) {
        if (ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate()) < 2){
            return 0;
        } else
            return initialPriceToPay*moreThanTwoDaysDiscount;
    }

    @Override
    public double calculateIfCustomerBookedMoreThan10NightsLastYearDiscount(Booking booking, double initialPriceToPay) {

        List<Booking> allCustomersBookings = bookingRepo.findByCustomer_Id(booking.getCustomer().getId());

        List<Booking> bookingsWithinOneYear = allCustomersBookings.stream().filter(b ->
                b.getStartDate().isAfter(LocalDate.now().minusYears(1)))
                .toList();

        Long totalAmountOfNightsBookedWithinOneYear = bookingsWithinOneYear
                .stream().mapToLong(this::calculateAmountOfNightsBooked).sum();

        if (totalAmountOfNightsBookedWithinOneYear >= 10){
            return initialPriceToPay*moreThan10DaysBookedDiscount;
        } else
            return 0.0;

    }

    @Override
    public Long calculateAmountOfNightsBooked(Booking booking) {
        return ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
    }


}
