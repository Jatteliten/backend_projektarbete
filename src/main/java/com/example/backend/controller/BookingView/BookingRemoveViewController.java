package com.example.backend.controller.BookingView;

import com.example.backend.model.Booking;
import com.example.backend.services.impl.BookingServicesImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Booking")
public class BookingRemoveViewController {
    private final BookingServicesImpl bs;

    public BookingRemoveViewController(BookingServicesImpl bs){
        this.bs = bs;
    }

    @RequestMapping("removeBooking")
    public String removeBooking(){
        return "Booking/removeBooking.html";
    }

    @PostMapping("removeBookingSuccess")
    public String removeBookingSuccess(@RequestParam String id){
        try {
            Long idLong = Long.parseLong(id);
            Booking b = bs.findById(idLong);
            if(b != null) {
                bs.deleteBooking(b);
            }else{
                return "Booking/errorBooking.html";
            }
        }catch(NumberFormatException e){
            return "Booking/errorBooking.html";
        }

        return "Booking/removeBookingSuccess.html";
    }


}