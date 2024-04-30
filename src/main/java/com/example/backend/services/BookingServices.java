package com.example.backend.services;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingServices {
    public DetailedBookingDto bookingToDetailedBookingDto(Booking b);
    public MiniBookingDto bookingToMiniBookingDto(Booking b);
    public Booking detailedBookingDtoToBooking(DetailedBookingDto b);
    public List<DetailedBookingDto> getAllDetailedBookings();
    public List<MiniBookingDto> getAllMiniBookings();
    public DetailedBookingDto getDetailedBookingById(Long id);
    public void addBooking(Booking b);
    public void deleteBooking(Booking b);

    public List<MiniBookingDto> findBookings(String searchWord);
    public MiniBookingDto getMiniBookingById(Long id);
    public void deleteBookingById(Long id);
    public String updateBooking(Long bookingId, LocalDate startDate, LocalDate endDate, Long roomId,int extraBeds);
    public boolean isAvailable(Long bookingId, LocalDate startDate, LocalDate endDate, Long roomId);
}
