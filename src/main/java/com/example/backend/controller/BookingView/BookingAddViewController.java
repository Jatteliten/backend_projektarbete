package com.example.backend.controller.BookingView;

import com.example.backend.Dto.BookingViews.AddBookingView;
import com.example.backend.model.Room;
import com.example.backend.services.impl.BookingServicesImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public String bookingSuccess(@RequestParam String email,
                                 @RequestParam Long roomId,
                                 @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB,Model model){
        String error = null;
        System.out.println(roomId);
        model.addAttribute("error",error);
        model.addAttribute("email",email);
        model.addAttribute("roomId",roomId);
        bookingServices.bookRoom(email,roomId,startDateB,endDateB);
        return "Booking/BookingSuccess.html";
    }
}
