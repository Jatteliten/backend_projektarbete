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

    private static final int AMOUNT_OF_DAYS_TO_GET_DISCOUNT = 2;
    private static final double MORE_THAN_TWO_DAYS_DISCOUNT = 0.005;
    private static final double SUNDAY_TO_MONDAY_DISCOUNT = 0.02;

    private static final double MORE_THAN_TEN_DAYS_BOOKED_DISCOUNT = 0.02;
    private static final int AMOUNT_OF_YEARS_TO_CHECK_WITHIN_FOR_DISCOUNT = 1;
    private static final int AMOUNT_OF_NIGHTS_WITHIN_YEAR_TO_GET_DISCOUNT = 10;



    private final BookingRepo bookingRepo;

    DiscountServicesImpl(BookingRepo bookingRepo){
        this.bookingRepo = bookingRepo;
    }

    @Override
    public double calculateTotalPriceWithAllDiscounts(Booking booking) {

        double totalPriceToPay  = calculateFullPrice(booking);
        double totalDiscount = 0;

        if (checkSundayToMondayDiscount(booking)){
            totalPriceToPay = applySundayToMondayDiscount(booking,totalPriceToPay);
        }

        if (checkMoreThanTwoNightsDiscount(booking)){
            totalDiscount += calculateMoreThanTwoNightsDiscount(totalPriceToPay);
        }

        if (checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(booking)){
            totalDiscount += calculateMoreThanTenNightsDiscount(totalPriceToPay);
        }

        return totalPriceToPay - totalDiscount;
    }

    @Override
    public double calculateFullPrice(Booking booking) {
        Long amountOfNightsBooked = ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate());
        return amountOfNightsBooked * booking.getRoom().getPricePerNight();
    }

    @Override
    public boolean checkSundayToMondayDiscount(Booking booking){
        LocalDate startDate = booking.getStartDate();
        while (startDate.isBefore(booking.getEndDate())){
            if (startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) &&
                    startDate.plusDays(1).getDayOfWeek().equals(DayOfWeek.MONDAY)){
                return true;
            }
            startDate = startDate.plusDays(1);
        }
        return false;
    }

    @Override
    public double applySundayToMondayDiscount(Booking booking,double fullPrice) {
        //returns the initial price to pay after standars sunday to monday discount
        LocalDate startDate = booking.getStartDate();
        double discount = 0;

        while (startDate.isBefore(booking.getEndDate())){
            if (startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) &&
                    startDate.plusDays(1).getDayOfWeek().equals(DayOfWeek.MONDAY)){
                discount += booking.getRoom().getPricePerNight()* SUNDAY_TO_MONDAY_DISCOUNT;
            }
            startDate = startDate.plusDays(1);
        }

        return fullPrice - discount;
    }

    @Override
    public boolean checkMoreThanTwoNightsDiscount(Booking booking) {
        return ChronoUnit.DAYS.between(booking.getStartDate(),booking.getEndDate()) >= AMOUNT_OF_DAYS_TO_GET_DISCOUNT;

    }
    @Override
    public double calculateMoreThanTwoNightsDiscount(double initialPriceToPay) {
            return initialPriceToPay* MORE_THAN_TWO_DAYS_DISCOUNT;
    }

    @Override
    public long calculateAmountsOfNightsCustomerBookedWithinOneYear(Booking booking){
        List<Booking> allCustomersBookings = bookingRepo.findByCustomer_Id(booking.getCustomer().getId());

        List<Booking> bookingsWithinOneYear = allCustomersBookings.stream().filter(b ->
                        b.getStartDate().isAfter(LocalDate.now()
                                .minusYears(AMOUNT_OF_YEARS_TO_CHECK_WITHIN_FOR_DISCOUNT)))
                .toList();

        return bookingsWithinOneYear
                .stream().mapToLong(this::calculateAmountOfNightsBooked).sum();
    }

    @Override
    public boolean checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(Booking booking){
        return calculateAmountOfNightsBooked(booking) >= AMOUNT_OF_NIGHTS_WITHIN_YEAR_TO_GET_DISCOUNT;
    }

    @Override
    public double calculateMoreThanTenNightsDiscount(double initialPriceToPay) {
        return initialPriceToPay* MORE_THAN_TEN_DAYS_BOOKED_DISCOUNT;
    }

    @Override
    public Long calculateAmountOfNightsBooked(Booking booking) {
        return ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
    }

}
