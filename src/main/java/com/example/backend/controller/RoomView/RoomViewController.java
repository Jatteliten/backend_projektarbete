package com.example.backend.controller.RoomView;


import com.example.backend.services.RoomServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Room")
public class RoomViewController {

    RoomServices roomServices;

    public RoomViewController(RoomServices roomServices) {
        this.roomServices = roomServices;
    }

    @GetMapping("/all")
    public String getBlacklist(Model model) {
        model.addAttribute("rooms", roomServices.getAllDetailedRooms());
        return "Room/room.html";
    }

    @RequestMapping("/backlog/{id}")
    public String getBacklog(Model model, @PathVariable Long id) {
        model.addAttribute("RoomdId", id);
        //Filtrera ut alla door och cleaning events med detta id
        return "Room/backlog.html";
    }
}
