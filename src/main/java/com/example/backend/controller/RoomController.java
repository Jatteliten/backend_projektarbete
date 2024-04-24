package com.example.backend.controller;

import com.example.backend.Dto.RoomViews.DetailedRoomDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.services.RoomServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomServices rs;

    public RoomController(RoomServices rs){
        this.rs = rs;
    }

    @RequestMapping("/getAll")
    List<DetailedRoomDto> getAllRooms(){
        return rs.getAllDetailedRooms();
    }

    @RequestMapping("/getAllMini")
    List<MiniRoomDto> getAllMiniRooms() { return rs.getAllMiniRooms();}

    @RequestMapping("/getBooking/{id}")
    DetailedRoomDto getRoomById(@PathVariable Long id){
        return rs.getDetailedRoomById(id);
    }
}
