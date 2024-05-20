package com.example.backend.services.impl;


import com.example.backend.model.Room;
import com.example.backend.model.modelUti.CleaningEvent;
import com.example.backend.model.modelUti.DoorEvent;
import com.example.backend.repos.CleaningEventRepo;
import com.example.backend.repos.DoorEventRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class RoomEventServicesImplTests {

    @Mock
    private DoorEventRepo doorEventRepo;

    @Mock
    private CleaningEventRepo cleaningEventRepo;

    private RoomEventServicesImpl roomEventServices;
    Room room;
    LocalDateTime now;

    CleaningEvent cleaningEvent1 = new CleaningEvent();
    CleaningEvent cleaningEvent2 = new CleaningEvent();
    DoorEvent doorEvent1 = new DoorEvent();
    DoorEvent doorEvent2 = new DoorEvent();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roomEventServices = new RoomEventServicesImpl(doorEventRepo, cleaningEventRepo);
        room = new Room();
        room.setId(1L);
        now = LocalDateTime.now();

        cleaningEvent1.setRoom(room);
        cleaningEvent1.setTimeStamp(now.minusHours(2));
        cleaningEvent1.setType("Cleaning Started");
        cleaningEvent1.setCleaningByUser("User1");

        cleaningEvent2.setRoom(room);
        cleaningEvent2.setTimeStamp(now.minusHours(1));
        cleaningEvent2.setType("Cleaning Finished");
        cleaningEvent2.setCleaningByUser("User2");

        doorEvent1.setRoom(room);
        doorEvent1.setTimeStamp(now.minusHours(1));
        doorEvent1.setType("Door Opened");

        doorEvent2.setRoom(room);
        doorEvent2.setTimeStamp(now.minusMinutes(30));
        doorEvent2.setType("Door Closed");
    }


    @Test
    public void testGetEventsByRoomId() {

        List<CleaningEvent> cleaningEvents = List.of(cleaningEvent1, cleaningEvent2);
        List<DoorEvent> doorEvents = List.of(doorEvent2);

        when(cleaningEventRepo.findByRoomId(anyLong())).thenReturn(cleaningEvents);
        when(doorEventRepo.findByRoomId(anyLong())).thenReturn(doorEvents);

        List<Object> events = roomEventServices.getEventsByRoomId(1L);

        assertEquals(3, events.size());
        assertEquals(cleaningEvent1, events.get(0));
        assertEquals(cleaningEvent2, events.get(1));
        assertEquals(doorEvent2, events.get(2));
    }


    @Test
    public void testGetCleaningEventsByRoomId() {

        List<CleaningEvent> cleaningEvents = List.of(cleaningEvent1, cleaningEvent2);

        when(cleaningEventRepo.findByRoomId(anyLong())).thenReturn(cleaningEvents);
        List<CleaningEvent> retrievedCleaningEvents = roomEventServices.getCleaningEventsByRoomId(1L);

        assertEquals(2, retrievedCleaningEvents.size());
        assertEquals(cleaningEvent1, retrievedCleaningEvents.get(0));
        assertEquals(cleaningEvent2, retrievedCleaningEvents.get(1));
    }

    @Test
    public void testGetDoorEventsByRoomId() {

        List<DoorEvent> doorEvents = List.of(doorEvent1, doorEvent2);

        when(doorEventRepo.findByRoomId(anyLong())).thenReturn(doorEvents);
        List<DoorEvent> retrievedDoorEvents = roomEventServices.getDoorEventsByRoomId(1L);

        assertEquals(2, retrievedDoorEvents.size());
        assertEquals(doorEvent1, retrievedDoorEvents.get(0));
        assertEquals(doorEvent2, retrievedDoorEvents.get(1));
    }
}
