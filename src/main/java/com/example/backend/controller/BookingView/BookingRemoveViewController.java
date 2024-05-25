package com.example.backend.controller.BookingView;

import com.example.backend.model.Booking;
import com.example.backend.services.BookingServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/Booking")
public class BookingRemoveViewController {
    private final BookingServices bs;

    public BookingRemoveViewController(BookingServices bs){
        this.bs = bs;
    }

    @RequestMapping("/removeBooking")
    @PreAuthorize("isAuthenticated()")
    public String removeBooking(){
        return "Booking/removeBooking";
    }

    @PostMapping("/removeBookingSuccess")
    @PreAuthorize("isAuthenticated()")
    public String removeBookingSuccess(@RequestParam String id, Model model){
        try {
            Long idLong = Long.parseLong(id);
            Booking b = bs.findById(idLong);
            bs.deleteBooking(b);
        }catch(NumberFormatException e){
            model.addAttribute("header", "Only enter numbers");
            return "Booking/removeBooking";
        }catch(NoSuchElementException e){
            model.addAttribute("header", "Booking with given Id does not exist");
            return "Booking/removeBooking";
        }

        model.addAttribute("bookingId", id);
        return "Booking/removeBookingSuccess";
    }


}