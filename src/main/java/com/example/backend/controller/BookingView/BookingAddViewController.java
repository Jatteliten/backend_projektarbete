package com.example.backend.controller.BookingView;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/Booking")
public class BookingAddViewController {
    private final BookingServices bookingServices;
    private final CustomerServices customerServices;
    private final RoomServices roomServices;
    private final BlacklistServices blacklistServices;
    private final DiscountServices discountServices;

    public BookingAddViewController(BookingServices bookingServices, CustomerServices customerServices,
                                    RoomServices roomServices, BlacklistServices blacklistServices,
                                    DiscountServices discountServices) {
        this.bookingServices = bookingServices;
        this.customerServices = customerServices;
        this.roomServices = roomServices;
        this.blacklistServices = blacklistServices;
        this.discountServices = discountServices;
    }

    @RequestMapping("/availableRooms")
    public String findRooms(@RequestParam(required = false) Integer beds,
                            @RequestParam(required = false) Integer extraBeds,
                            @RequestParam(required = false) LocalDate startDate,
                            @RequestParam(required = false) LocalDate endDate,
                            Model model) {
        List<Room> rooms = bookingServices.filterRooms(beds, extraBeds, startDate, endDate);
        String error = null;

        if (startDate != null && startDate.isBefore(LocalDate.now())) {
            rooms = Collections.emptyList();
            error = "Start date must be in the future";
        }

        if (endDate != null && endDate.isBefore(startDate)) {
            rooms = Collections.emptyList();
            error = "End date must be after start date";
        }

        model.addAttribute("title", "Available rooms");
        model.addAttribute("listOfRooms", rooms);
        model.addAttribute("buttonText", "Book Room");
        model.addAttribute("error", error);
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);

        return "Booking/addBooking.html";
    }

    @RequestMapping("/BookingSuccess")
    public String bookingSuccess(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model) {


        String error = null;
        Customer bookingCustomer = customerServices.findByEmail(email);

        if (bookingCustomer == null) {
            error = "No customer with email: " + email + " found.";
        } else if (blacklistServices.isBlacklisted(email)) {
            error = "Customer with email " + email + " is blacklisted.";
        }

        if (error != null) {
            List<Room> rooms = List.of(roomServices.findById(roomId));
            model.addAttribute("title", "Available rooms");
            model.addAttribute("listOfRooms", rooms);
            model.addAttribute("buttonText", "Book Room");
            model.addAttribute("error", error);
            model.addAttribute("start", startDateB);
            model.addAttribute("end", endDateB);
            return "Booking/addBooking.html";
        }

        model.addAttribute("error", error);
        model.addAttribute("email", email);
        model.addAttribute("roomId", roomId);
        error = bookingServices.bookRoom(email, roomId, startDateB, endDateB);
        if (!error.contains("Success!")) {
            model.addAttribute("title", "Available rooms");
            model.addAttribute("listOfRooms", Collections.emptyList());
            model.addAttribute("buttonText", "Book Room");
            model.addAttribute("error", error);
            model.addAttribute("start", startDateB);
            model.addAttribute("end", endDateB);
            return "Booking/addBooking.html";
        }

        return "Booking/BookingSuccess.html";
    }
    @RequestMapping("/confirmBooking")
    public String confirmBooking(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model) {
        String error = null;
        Customer c = customerServices.findByEmail(email);
        if (c == null) {
            error = "No customer with email: " + email + " found.";
        } else if (blacklistServices.isBlacklisted(email)) {
            error = "Customer with email " + email + " is blacklisted.";
        }
        if (error != null) {
            List<Room> rooms = List.of(roomServices.findById(roomId));
            model.addAttribute("title", "Available rooms");
            model.addAttribute("listOfRooms", rooms);
            model.addAttribute("buttonText", "Book Room");
            model.addAttribute("error", error);
            model.addAttribute("start", startDateB);
            model.addAttribute("end", endDateB);
            return "Booking/addBooking.html";
        }

        Room r = roomServices.findById(roomId);
        Booking b = new Booking(1L,startDateB,endDateB,0,r,c);
        double fullPrice = discountServices.calculateFullPrice(b);
        double priceWithDiscount = discountServices.calculateTotalPriceWithDiscounts(b);
        model.addAttribute("fullPrice", fullPrice);
        model.addAttribute("discountedPrice", priceWithDiscount);
        model.addAttribute("email", email);
        model.addAttribute("roomId", roomId);
        model.addAttribute("start", startDateB);
        model.addAttribute("end", endDateB);

        return "Booking/confirmBooking.html";
    }
}
