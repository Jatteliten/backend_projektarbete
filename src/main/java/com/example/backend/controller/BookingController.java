package com.example.backend.controller;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.model.Booking;
import com.example.backend.repos.BookingRepo;
import com.example.backend.services.BookingServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingServices bs;

    public BookingController(BookingServices bs){
        this.bs = bs;
    }


    @RequestMapping("/getAll")
    List<DetailedBookingDto> getAllBookings(){
        return bs.getAllDetailedBookings();
    }

    @RequestMapping("/getAllMini")
    List<MiniBookingDto> getAllMiniBookings() { return bs.getAllMiniBookings();}

    @RequestMapping("/getBooking/{id}")
    DetailedBookingDto getBookingById(@PathVariable Long id){
        return bs.getDetailedBookingById(id);
    }

}
