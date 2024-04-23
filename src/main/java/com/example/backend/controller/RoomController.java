package com.example.backend.controller;

import com.example.backend.model.Room;
import com.example.backend.repos.RoomRepo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepo rr;

    public RoomController(RoomRepo rr){
        this.rr = rr;
    }

    @RequestMapping("/getAll")
    List<Room> getAllRooms(){
        return rr.findAll();
    }

    @RequestMapping("/getById/{id}")
    Room getRoomById(@PathVariable Long id){
        return rr.findById(id).get();
    }
}
