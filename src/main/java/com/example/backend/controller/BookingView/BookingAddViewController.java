package com.example.backend.controller.BookingView;

import com.example.backend.model.Room;
import com.example.backend.services.impl.BookingServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Booking")
public class BookingAddViewController {
    @Autowired
    BookingServicesImpl bookingServices;

    @RequestMapping("/availableRooms")
    public String findRooms(@RequestParam(required = false) Integer beds,
                            @RequestParam(required = false) Integer extraBeds,
                            @RequestParam(required = false) LocalDate startDate,
                            @RequestParam(required = false) LocalDate endDate,
                            Model model){
        List<Room> rooms = bookingServices.filterRooms(beds, extraBeds,startDate, endDate);
        String error = null;
        model.addAttribute("title","Available rooms");
        model.addAttribute("listOfRooms",rooms);
        model.addAttribute("buttonText","Book Room");
        model.addAttribute("error",error);
        return "Booking/addBooking.html";
    }
    @RequestMapping("/BookingSuccess")
    public String bookingSuccess(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
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
