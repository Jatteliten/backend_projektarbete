package com.example.backend.services;

import com.example.backend.Dto.RoomViews.DetailedRoomDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Room;

import java.util.List;

public interface RoomServices {
    public DetailedRoomDto roomToDetailedRoomDto(Room r);
    public MiniRoomDto roomToMiniRoomDto(Room r);
    public Room detailedRoomDtoToRoom(DetailedRoomDto r);
    public List<DetailedRoomDto> getAllDetailedRooms();
    public List<MiniRoomDto> getAllMiniRooms();
    public DetailedRoomDto getDetailedRoomById(Long id);
    public boolean roomExists(Long id);
    public Room findById(Long id);
}
