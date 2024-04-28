package com.example.backend.services;

import com.example.backend.Dto.BookingViews.AddBookingView;
import com.example.backend.Dto.BookingViews.BookingSuccessView;
import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.model.Booking;
import jakarta.validation.Valid;
import org.springframework.ui.Model;

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
    public String filterRooms(@Valid AddBookingView addBookingView, Model model);
    public String bookRoom(BookingSuccessView bookingSuccessView, Model model);
}
