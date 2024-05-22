package com.example.backend.controller.RoomView;


import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.services.RoomEventServices;
import com.example.backend.services.RoomServices;
import com.example.backend.services.impl.RoomEventServicesImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/Room")
public class RoomViewController {

    RoomServices roomServices;
    RoomEventServices roomEventServices;

    public RoomViewController(RoomServices roomServices, RoomEventServices roomEventServices) {
        this.roomServices = roomServices;
        this.roomEventServices = roomEventServices;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public String getBlacklist(Model model) {
        model.addAttribute("rooms", roomServices.getAllDetailedRooms());
        return "Room/room.html";
    }

    @RequestMapping("/backlog/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getBacklog(Model model, @PathVariable Long id) {
        model.addAttribute("header", "Room " + id);
        model.addAttribute("allEvents", roomEventServices.getEventsByRoomId(id));

        List<CleaningEvent> cleaningEvents = roomEventServices.getCleaningEventsByRoomId(id);
        List<DoorEvent> doorEvents = roomEventServices.getDoorEventsByRoomId(id);
        model.addAttribute("cleaningEvents", cleaningEvents);
        model.addAttribute("doorEvents", doorEvents);

        return "Room/backlog.html";
    }

}
