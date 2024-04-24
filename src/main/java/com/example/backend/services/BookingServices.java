package com.example.backend.services;

import com.example.backend.Dto.DetailedBookingDto;
import com.example.backend.Dto.MiniBookingDto;
import com.example.backend.Dto.MiniCustomerDto;
import com.example.backend.Dto.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.repos.BookingRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServices {
    private final BookingRepo br;

    public BookingServices(BookingRepo br){
        this.br = br;
    }

    public DetailedBookingDto BookingToDetailedBookingDto(Booking b){
        return DetailedBookingDto.builder().id(b.getId()).beds(b.getExtraBeds())
                .startDate(b.getStartDate()).endDate(b.getEndDate())
                .miniRoom(new MiniRoomDto())
                .miniCustomer(new MiniCustomerDto());
        id, beds, start date, end date, MiniRoom, MiniCustomer
    }

    public MiniBookingDto BookingToMiniBookingDto(Booking b){
        return MiniBookingDto.builder().id(b.getId()).startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .miniRoom(new MiniRoomDto())
                .miniCustomer(new MiniCustomerDto());
        id, start date, end date, MiniRoom, MiniCustomer
    }

    public List<DetailedBookingDto> getAllDetailedBookings(){
        br.findAll().stream().map(b -> BookingToDetailedBookingDto(b)).toList();
    }
}
