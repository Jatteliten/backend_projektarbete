package com.example.backend.services;

import com.example.backend.Dto.RoomViews.DetailedRoomDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Room;

import java.util.List;

public interface RoomServices {
    DetailedRoomDto roomToDetailedRoomDto(Room r);
    MiniRoomDto roomToMiniRoomDto(Room r);
    Room detailedRoomDtoToRoom(DetailedRoomDto r);
    List<DetailedRoomDto> getAllDetailedRooms();
    List<MiniRoomDto> getAllMiniRooms();
    DetailedRoomDto getDetailedRoomById(Long id);
    boolean roomExists(Long id);
    Room findById(Long id);
}
