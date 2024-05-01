package com.example.backend.services;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingServices {
    DetailedBookingDto bookingToDetailedBookingDto(Booking b);
    MiniBookingDto bookingToMiniBookingDto(Booking b);
    Booking detailedBookingDtoToBooking(DetailedBookingDto b);
    List<DetailedBookingDto> getAllDetailedBookings();
    List<MiniBookingDto> getAllMiniBookings();
    DetailedBookingDto getDetailedBookingById(Long id);
    void addBooking(Booking b);
    void deleteBooking(Booking b);
    List<MiniBookingDto> findBookings(String searchWord);
    MiniBookingDto getMiniBookingById(Long id);
    void deleteBookingById(Long id);
    String updateBooking(Long bookingId, LocalDate startDate, LocalDate endDate, Long roomId,int extraBeds);
}
