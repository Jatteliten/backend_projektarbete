package com.example.backend.controller.RoomView;


import com.example.backend.services.RoomServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Room")
public class RoomViewController {

    RoomServices roomServices;

    @GetMapping("/all")
    public String getBlacklist(Model model) {
        model.addAttribute("rooms", roomServices.getAllDetailedRooms());
        return "Room/room.html";
    }
}
