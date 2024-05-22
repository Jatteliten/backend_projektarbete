package com.example.backend.controller.BookingView;

import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.services.BookingServices;
import com.example.backend.services.CustomerServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/Booking")
public class BookingUpdateViewController {

    private final BookingServices bookingServices;

    private final CustomerServices customerServices;

    public BookingUpdateViewController(BookingServices bookingServices, CustomerServices customerServices) {
        this.bookingServices = bookingServices;
        this.customerServices = customerServices;
    }

    @RequestMapping("/allWithUpdate")
    public String allWithUpdate(Model model) {
        List<MiniBookingDto> bookings = bookingServices.getAllMiniBookings();
        model.addAttribute("allBookings", bookings);
        model.addAttribute("header", "All Bookings");
        return "Booking/updateBooking.html";
    }

    @RequestMapping("/filter/update")
    public String filter(@RequestParam String input, Model model) {
        List<MiniBookingDto> bookings = bookingServices.findBookings(input);

        if (!bookings.isEmpty()) {
            model.addAttribute("allBookings", bookings);
            model.addAttribute("header", "Matches Found");
        } else {
            model.addAttribute("header", "No Matches Found");
        }
        return "Booking/updateBooking.html";
    }

    @RequestMapping("/update/availableRooms")
    public String findRooms(@RequestParam(required = false) Long id,
                            @RequestParam(required = false) Long custId,
                            @RequestParam(required = false) Integer beds,
                            @RequestParam(required = false) Integer extraBeds,
                            @RequestParam(required = false) LocalDate startDate,
                            @RequestParam(required = false) LocalDate endDate,
                            Model model) {

        MiniCustomerDto customer = customerServices.getMiniCustomerById(custId);

        model.addAttribute("oldBookingId", id);
        model.addAttribute("custId", custId);

        model.addAttribute("custFirstName", customer.getFirstName());
        model.addAttribute("custLastName", customer.getLastName());
        model.addAttribute("custEmail", customer.getEmail());
        model.addAttribute("custPhoneNr", customer.getPhoneNumber());
        model.addAttribute("extraBeds", extraBeds);

        List<Room> rooms = bookingServices.filterRooms(beds, extraBeds, startDate, endDate);
        String error = null;

        if (startDate != null && startDate.isBefore(LocalDate.now())){
            rooms = Collections.emptyList();
            error = "Start date must be in the future";
        }

        if (endDate != null &&endDate.isBefore(startDate)){
            rooms = Collections.emptyList();
            error = "End date must be after start date";
        }

        model.addAttribute("title", "Available rooms");
        model.addAttribute("listOfRooms", rooms);
        model.addAttribute("buttonText", "Update Room");
        model.addAttribute("error", error);
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);
        return "Booking/updateBookingForm.html";
    }

    @RequestMapping("/update/BookingSuccess")
    public String bookingSuccess(@RequestParam Long oldBookingId,
                                 @RequestParam int extraBeds,
                                 @RequestParam String email,
                                 @RequestParam Long roomId,
                                 @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model) {




        String error = bookingServices.updateBooking(oldBookingId,startDateB,endDateB,roomId,extraBeds);
        if (!error.contains("Success!")){
            System.out.println("här");
            Customer c = customerServices.findByEmail(email);
            model.addAttribute("oldBookingId", oldBookingId);
            model.addAttribute("custId", c.getId());

            model.addAttribute("custFirstName", c.getFirstName());
            model.addAttribute("custLastName", c.getLastName());
            model.addAttribute("custEmail", c.getEmail());
            model.addAttribute("custPhoneNr", c.getPhoneNumber());

            List<Room> rooms = Collections.emptyList();

            model.addAttribute("title", "Available rooms");
            model.addAttribute("listOfRooms", rooms);
            model.addAttribute("buttonText", "Update Room");
            model.addAttribute("error", error);


            return "Booking/updateBookingForm.html";
        }
        System.out.println(roomId);
        model.addAttribute("error", error);
        model.addAttribute("email", email);
        model.addAttribute("roomId", roomId);

        return "Booking/BookingSuccess.html";
    }

    @RequestMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, Model model) {
        bookingServices.deleteBookingById(id);
        model.addAttribute("message", "Booking cancelled!");
        return allWithUpdate(model);
    }
}
