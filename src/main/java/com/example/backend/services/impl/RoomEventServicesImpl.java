package com.example.backend.services.impl;

import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import com.example.backend.services.RoomEventServices;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomEventServicesImpl implements RoomEventServices {

    private final DoorEventRepo doorEventRepo;
    private final CleaningEventRepo cleaningEventRepo;

    public RoomEventServicesImpl(DoorEventRepo doorEventRepo, CleaningEventRepo cleaningEventRepo) {
        this.doorEventRepo = doorEventRepo;
        this.cleaningEventRepo = cleaningEventRepo;
    }

    public List<Object> getEventsByRoomId(Long id) {
        List<CleaningEvent> cleaningEvents = getCleaningEventsByRoomId(id);
        List<DoorEvent> doorEvents = getDoorEventsByRoomId(id);

        return Stream.concat(cleaningEvents.stream(), doorEvents.stream()).sorted(Comparator.comparing(event -> {
            if (event instanceof CleaningEvent) {
                return ((CleaningEvent) event).getTimeStamp();
            } else if (event instanceof DoorEvent) {
                return ((DoorEvent) event).getTimeStamp();
            }
            return null;
        })).collect(Collectors.toList());
    }

    public List<CleaningEvent> getCleaningEventsByRoomId(Long id) {
        return cleaningEventRepo.findByRoomId(id);
    }
    public List<DoorEvent> getDoorEventsByRoomId(Long id) {
        return doorEventRepo.findByRoomId(id);
    }

}


