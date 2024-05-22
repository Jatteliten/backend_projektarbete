package com.example.backend.controller.BookingView;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
    private SendEmailServices sendEmail;
    private HttpSession session;

    public BookingAddViewController(BookingServices bookingServices, CustomerServices customerServices,
                                    RoomServices roomServices, BlacklistServices blacklistServices,
                                    DiscountServices discountServices, SendEmailServices sendEmail,
                                    HttpSession session) {
        this.bookingServices = bookingServices;
        this.customerServices = customerServices;
        this.roomServices = roomServices;
        this.blacklistServices = blacklistServices;
        this.discountServices = discountServices;
        this.sendEmail = sendEmail;
        this.session = session;
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

        return "Booking/addBooking.html";
    }

    @RequestMapping("/BookingSuccess")
    @PreAuthorize("isAuthenticated()")
    public String bookingSuccess(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB,Model model) {


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
            return "Booking/addBooking.html";
        }


        sendConfirmDetailsEmail(email);

        return "Booking/BookingSuccess.html";
    }
    @RequestMapping("/confirmBooking")
    @PreAuthorize("isAuthenticated()")
    public String confirmBooking(@RequestParam String email, @RequestParam Long roomId, @RequestParam LocalDate startDateB,
                                 @RequestParam LocalDate endDateB, Model model, HttpServletRequest request) {
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
        double sundayDiscountedPrice = fullPrice;
        double priceToPay = discountServices.calculateTotalPriceWithDiscounts(b);

        model.addAttribute("customerName", c.getFirstName());
        model.addAttribute("customerPhone", c.getPhoneNumber());
        model.addAttribute("pricePerNight", r.getPricePerNight());
        model.addAttribute("roomSize", r.getSize());
        model.addAttribute("amountOfNights", discountServices.calculateAmountOfNightsBooked(b));

        double sundayDiscount = 0.00;
        if (discountServices.checkSundayToMondayDiscount(b)){
            sundayDiscount = fullPrice - discountServices.applySundayToMondayDiscount(b,fullPrice);
            sundayDiscountedPrice = discountServices.applySundayToMondayDiscount(b,fullPrice);

        }
        model.addAttribute("sundayDiscount", sundayDiscount);

        double longStayDiscount = 0.00;
        if (discountServices.checkMoreThanTwoNightsDiscount(b)){
            longStayDiscount = discountServices.calculateMoreThanTwoNightsDiscount(sundayDiscountedPrice);
        }
        model.addAttribute("longStayDiscount", longStayDiscount);

        double tenDayDiscount = 0.00;
        if (discountServices.checkIfCustomerHaveMoreThanTenBookingNightsWithinAYear(b)){
            tenDayDiscount = discountServices.calculateMoreThanTenNightsDiscount(sundayDiscountedPrice);
        }
        model.addAttribute("tenDayDiscount", tenDayDiscount);

        model.addAttribute("fullPrice", fullPrice);
        model.addAttribute("discountedPrice", priceToPay);
        model.addAttribute("email", email);
        model.addAttribute("roomId", roomId);
        model.addAttribute("start", startDateB);
        model.addAttribute("end", endDateB);

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }

        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(model.asMap());
        session.setAttribute("modelMap", modelMap);



        return "Booking/confirmBooking.html";
    }

    private void sendConfirmDetailsEmail(String to) {
        ModelMap storedModel = (ModelMap) session.getAttribute("modelMap");
        for (Map.Entry<String, Object> entry : storedModel.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // email sender test
//        Map<String, Object> templateModel = new HashMap<>();
//        templateModel.put("test1", "test från controller");
//        templateModel.put("test2", "text från controller");
//
//        ModelMap modelMap = new ModelMap();
//        modelMap.addAllAttributes(model.asMap());


        try {
            sendEmail.sendConfirmationEmail(to, "Booking Confirmed", storedModel);
            System.out.println("Email sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error while sending email");
        }
    }
}
