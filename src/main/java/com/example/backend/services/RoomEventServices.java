package com.example.backend.services;

import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;

import java.util.List;

public interface RoomEventServices {

    List<Object> getEventsByRoomId(Long id);
    List<CleaningEvent> getCleaningEventsByRoomId(Long id);
    List<DoorEvent> getDoorEventsByRoomId(Long id);
}
