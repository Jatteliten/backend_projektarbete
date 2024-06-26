package com.example.backend.controller.BookingView;

import com.example.backend.EmailSender;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.services.*;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Booking")
public class BookingAddViewController {
    private final BookingServices bookingServices;
    private final CustomerServices customerServices;
    private final RoomServices roomServices;
    private final BlacklistServices blacklistServices;
    private final DiscountServices discountServices;
    private final EmailSender emailSender;


    public BookingAddViewController(BookingServices bookingServices, CustomerServices customerServices,
                                    RoomServices roomServices, BlacklistServices blacklistServices,
                                    DiscountServices discountServices, EmailSender emailSender
    ) {
        this.bookingServices = bookingServices;
        this.customerServices = customerServices;
        this.roomServices = roomServices;
        this.blacklistServices = blacklistServices;
        this.discountServices = discountServices;
        this.emailSender = emailSender;
    }

    @RequestMapping("/availableRooms")
    @PreAuthorize("isAuthenticated()")
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

        return "Booking/addBooking";
    }

    @RequestMapping("/BookingSuccess")
    @PreAuthorize("isAuthenticated()")
    public String bookingSuccess(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model) {


        String error = null;

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
            return "Booking/addBooking";
        }

        Customer c = customerServices.findByEmail(email);
        Room r = roomServices.findById(roomId);
        Booking b = new Booking(1L,startDateB,endDateB,0,r,c);
        Map<String, Object> modelMap = createTemplateModel(b, c, r, discountServices, email, roomId, startDateB, endDateB);

        try {
            emailSender.sendEmailWithDatabaseTemplate(email,"Booking Confirmed",modelMap,"confirmBookingEmailTemplate");
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            System.out.println("Error while sending email");
        }
        return "Booking/BookingSuccess";
    }
    @RequestMapping("/confirmBooking")
    @PreAuthorize("isAuthenticated()")
    public String confirmBooking(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model) {
        String error = null;
        Customer c = customerServices.findByEmail(email);


        if (blacklistServices.isBlacklisted(email)) {
            error = "Customer with email " + email + " is blacklisted.";
        } else if (c == null) {
            error = "No customer with email: " + email + " found.";
        }

        if (error != null) {
            List<Room> rooms = List.of(roomServices.findById(roomId));
            model.addAttribute("title", "Available rooms");
            model.addAttribute("listOfRooms", rooms);
            model.addAttribute("buttonText", "Book Room");
            model.addAttribute("error", error);
            model.addAttribute("start", startDateB);
            model.addAttribute("end", endDateB);
            return "Booking/addBooking";
        }

        Room r = roomServices.findById(roomId);
        Booking b = new Booking(1L,startDateB,endDateB,0,r,c);

        Map<String, Object> modelMap = createTemplateModel(b, c, r, discountServices, email, roomId, startDateB, endDateB);
        addAttributesToModel(model, modelMap);



        return "Booking/confirmBooking";
    }


    private Map<String, Object> createTemplateModel(Booking b, Customer c, Room r, DiscountServices discountServices, String email, Long roomId, LocalDate startDateB, LocalDate endDateB) {
        Map<String, Object> modelMap = new HashMap<>();

        double fullPrice = discountServices.calculateFullPrice(b);
        double sundayDiscountedPrice = fullPrice;
        double priceToPay = discountServices.calculateTotalPriceWithDiscounts(b);

        modelMap.put("customerName", c.getFirstName());
        modelMap.put("customerPhone", c.getPhoneNumber());
        modelMap.put("pricePerNight", r.getPricePerNight());
        modelMap.put("roomSize", r.getSize());
        modelMap.put("amountOfNights", discountServices.calculateAmountOfNightsBooked(b));

        double sundayDiscount = 0.00;
        if (discountServices.checkSundayToMondayDiscount(b)) {
            sundayDiscount = fullPrice - discountServices.applySundayToMondayDiscount(b, fullPrice);
            sundayDiscountedPrice = discountServices.applySundayToMondayDiscount(b, fullPrice);
        }
        modelMap.put("sundayDiscount", sundayDiscount);

        double longStayDiscount = 0.00;
        if (discountServices.checkMoreThanTwoNightsDiscount(b)) {
            longStayDiscount = discountServices.calculateMoreThanTwoNightsDiscount(sundayDiscountedPrice);
        }
        modelMap.put("longStayDiscount", longStayDiscount);

        double tenDayDiscount = 0.00;
        if (discountServices.checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(b)) {
            tenDayDiscount = discountServices.calculateMoreThanTenNightsDiscount(sundayDiscountedPrice);
        }
        modelMap.put("tenDayDiscount", tenDayDiscount);

        modelMap.put("fullPrice", fullPrice);
        modelMap.put("discountedPrice", priceToPay);
        modelMap.put("email", email);
        modelMap.put("roomId", roomId);
        modelMap.put("start", startDateB);
        modelMap.put("end", endDateB);

        return modelMap;
    }

    private void addAttributesToModel(Model model, Map<String, Object> modelMap) {
        for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }
    }
}
