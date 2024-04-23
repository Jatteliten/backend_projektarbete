package com.example.backend.controller;

import com.example.backend.model.Booking;
import com.example.backend.repos.BookingRepo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingRepo br;

    public BookingController(BookingRepo br){
        this.br = br;
    }


    @RequestMapping("/getAll")
    List<Booking> getAllRooms(){
        return br.findAll();
    }

    @RequestMapping("/getById/{id}")
    Booking getRoomById(@PathVariable Long id){
        return br.findById(id).get();
    }


}
