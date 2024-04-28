package com.example.backend.controller.BookingView;

import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.services.impl.BookingServicesImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Booking")
public class BookingUpdateViewController {

    private final BookingServicesImpl bookingServices;

    public BookingUpdateViewController(BookingServicesImpl bookingServices) {
        this.bookingServices = bookingServices;
    }

    @RequestMapping("/allWithUpdate")
    public String allWithUpdate(Model model) {
        List<MiniBookingDto> bookings = bookingServices.getAllMiniBookings();
        model.addAttribute("allBookings", bookings);
        model.addAttribute("header", "All Bookings");
        return "Booking/updateBooking.html";
    }

    @PostMapping("/filter/update")
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

    @RequestMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        MiniBookingDto miniBookingDto = bookingServices.getMiniBookingById(id);
        model.addAttribute("bookingId", id);
        model.addAttribute("startDate", miniBookingDto.getStartDate());
        model.addAttribute("endD ate", miniBookingDto.getEndDate());

        //Kanske vore bra att lista alla lediga rum så att man kan
        //skriva in id på det rum man vill byta till
        model.addAttribute("roomId", miniBookingDto.getMiniRoomDto().getId());
        //roomSize fältet bör inte kunna ändras i formuläret. Det ska bara visas för att ge info.
        model.addAttribute("roomSize", miniBookingDto.getMiniRoomDto().getSize());

        //Sida men ifylld form redo att ändras.
        return "Customer/updateBookingForm.html";
    }

    //Ska kallas på när man fyllt i formulär
    @PostMapping("/update/final")
    public String updateByAll(@RequestParam Long bookingId,
                              @RequestParam LocalDate startDate,
                              @RequestParam LocalDate endDate,
                              @RequestParam Long roomId,
                              Model model) {

        // 1. Kolla att rummet är ledigt de nya datumen, även om man bytt rum
        // 2. Om allt är ok, uppdatera bokningen.
        // Om inte ladda om sidan med formuläret och skicka med lämpligt felmeddelande.

        boolean isAvailable = bookingServices.isAvailable(bookingId, startDate, endDate, roomId);
        if (isAvailable) {
            bookingServices.updateBooking(bookingId, startDate, endDate, roomId);
            model.addAttribute("message", "Customer updated successfully!");
            return allWithUpdate(model);
        } else {
            //för att meddelandet ska skickas med måste jag väl anropa metoden med model?
            //men hur får jag då med Id som måste vara med i pathen?
            model.addAttribute("message", "Booking not available!");
            return "Booking/update/{bookingId}";
        }

    }

    @RequestMapping("cancel/{id}")
    public String cancel(@PathVariable Long id, Model model) {
        bookingServices.deleteBookingById(id);
        model.addAttribute("message", "Customer cancelled!");
        return allWithUpdate(model);
    }
}
