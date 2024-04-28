package com.example.backend.controller.BookingView;

import com.example.backend.Dto.BookingViews.AddBookingView;
import com.example.backend.Dto.BookingViews.BookingSuccessView;

import com.example.backend.services.impl.BookingServicesImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/Booking")
public class BookingAddViewController {
    @Autowired
    BookingServicesImpl bookingServices;

    @RequestMapping("/availableRooms")
    public String findRooms(@Valid AddBookingView bookingView, Model model) {
        return bookingServices.filterRooms(bookingView,model);
    }

    @RequestMapping("/BookingSuccess")
    public String bookingSuccess(BookingSuccessView bookingSuccessView, Model model){
        return bookingServices.bookRoom(bookingSuccessView, model);
    }
}
