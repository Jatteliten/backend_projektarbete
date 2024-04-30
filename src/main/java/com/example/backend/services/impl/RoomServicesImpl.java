package com.example.backend.services.impl;

import com.example.backend.Dto.RoomViews.DetailedRoomDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Room;
import com.example.backend.repos.RoomRepo;
import com.example.backend.services.RoomServices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServicesImpl implements RoomServices {
    private final RoomRepo rr;

    public RoomServicesImpl(RoomRepo rr){
        this.rr = rr;
    }

    public DetailedRoomDto roomToDetailedRoomDto(Room r){
        return DetailedRoomDto.builder().id(r.getId()).size(r.getSize()).build();
    }

    public MiniRoomDto roomToMiniRoomDto(Room r){
        return MiniRoomDto.builder().id(r.getId()).size(r.getSize()).build();
    }

    public Room detailedRoomDtoToRoom(DetailedRoomDto r){
        return Room.builder().id(r.getId()).size(r.getSize()).build();
    }

    public List<DetailedRoomDto> getAllDetailedRooms(){
        return rr.findAll().stream().map(r -> roomToDetailedRoomDto(r)).toList();
    }

    public List<MiniRoomDto> getAllMiniRooms(){
        return rr.findAll().stream().map(r -> roomToMiniRoomDto(r)).toList();
    }

    public DetailedRoomDto getDetailedRoomById(Long id){
        return roomToDetailedRoomDto(rr.findById(id).get());
    }

    @Override
    public boolean roomExists(Long id) {
        Room room = rr.findById(id).orElse(null);
        return room != null;
    }

    @Override
    public Room findById(Long id) {
        return rr.findById(id).get();
    }

}
