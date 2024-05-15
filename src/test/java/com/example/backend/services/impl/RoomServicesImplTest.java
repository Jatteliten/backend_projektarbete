package com.example.backend.services.impl;

import com.example.backend.Dto.RoomViews.DetailedRoomDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Room;
import com.example.backend.repos.RoomRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RoomServicesImplTest {
    @Autowired
    RoomServicesImpl roomServices;

    @Mock
    private RoomRepo roomRepo;

    Room r1 = new Room(1L,4,4);
    DetailedRoomDto d1 = new DetailedRoomDto(1L,4);



    @Test
    void roomToDetailedRoomDtoTest() {
        DetailedRoomDto dr = roomServices.roomToDetailedRoomDto(r1);

        assertEquals(1l, dr.getId());
        assertEquals(4, dr.getSize());

    }

    @Test
    void roomToMiniRoomDtoTest() {
        MiniRoomDto mr = roomServices.roomToMiniRoomDto(r1);

        assertEquals(1l, mr.getId());
        assertEquals(4, mr.getSize());
    }

    @Test
    void detailedRoomDtoToRoomTest() {
        Room r = roomServices.detailedRoomDtoToRoom(d1);

        assertEquals(1l, r.getId());
        assertEquals(4, r.getSize());
    }

    @Test
    void getAllDetailedRoomsTest() {
        when(roomRepo.findAll()).thenReturn(Arrays.asList(r1));
        RoomServicesImpl services = new RoomServicesImpl(roomRepo);
        List<DetailedRoomDto> allDRD = services.getAllDetailedRooms();

        assertTrue(allDRD.size() == 1);
    }

    @Test
    void getAllMiniRoomsTest() {
        when(roomRepo.findAll()).thenReturn(Arrays.asList(r1));
        RoomServicesImpl services = new RoomServicesImpl(roomRepo);
        List<MiniRoomDto> allMR = services.getAllMiniRooms();

        assertTrue(allMR.size() == 1);
    }

    @Test
    void getDetailedRoomByIdTest() {
        when(roomRepo.findById(1L)).thenReturn(Optional.of(r1));
        RoomServicesImpl services = new RoomServicesImpl(roomRepo);
        DetailedRoomDto foundDR = services.getDetailedRoomById(1L);

        assertEquals(1l, foundDR.getId());
        assertEquals(4,foundDR.getSize());
    }

    @Test
    void roomExistsTest() {
        RoomServicesImpl services = new RoomServicesImpl(roomRepo);
        //när ett rum finns
        when(roomRepo.findById(1L)).thenReturn(Optional.of(new Room()));
        assertTrue(services.roomExists(1L));
        //när ett rum itne finns
        when(roomRepo.findById(2L)).thenReturn(Optional.empty());
        assertFalse(services.roomExists(2L));
    }

    @Test
    void findByIdTest() {
        RoomServicesImpl services = new RoomServicesImpl(roomRepo);

        when(roomRepo.findById(1L)).thenReturn(Optional.of(r1));

        Room foundRoom = services.findById(1L);

        assertEquals(1L, foundRoom.getId().longValue());
    }
}