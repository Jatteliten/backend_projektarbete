package com.example.backend.services;

import com.example.backend.repos.BookingRepo;
import org.springframework.stereotype.Service;

@Service
public class BookingServices {
    private final BookingRepo br;

    public BookingServices(BookingRepo br){
        this.br = br;
    }
}
