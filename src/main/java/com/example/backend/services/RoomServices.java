package com.example.backend.services;

import com.example.backend.repos.RoomRepo;
import org.springframework.stereotype.Service;

@Service
public class RoomServices {
    private final RoomRepo rr;

    public RoomServices(RoomRepo rr){
        this.rr = rr;
    }
}
