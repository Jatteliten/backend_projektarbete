package com.example.backend.services;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServices {
    private final BookingRepo br;

    private final CustomerRepo cr;
    private final RoomRepo rr;

    public BookingServices(BookingRepo br, CustomerRepo cr, RoomRepo rr){
        this.br = br;
        this.cr = cr;
        this.rr = rr;
    }

    public DetailedBookingDto bookingToDetailedBookingDto(Booking b){
        return DetailedBookingDto.builder().id(b.getId()).extraBeds(b.getExtraBeds())
                .startDate(b.getStartDate()).endDate(b.getEndDate())
                .miniRoomDto(new MiniRoomDto(b.getRoom().getId(), b.getRoom().getSize()))
                .miniCustomerDto(createMiniCustomerDtoFromCustomer(b.getCustomer())).build();
    }

    public MiniBookingDto bookingToMiniBookingDto(Booking b){
        return MiniBookingDto.builder().id(b.getId()).startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .miniRoomDto(new MiniRoomDto(b.getRoom().getId(), b.getRoom().getSize()))
                .miniCustomerDto(createMiniCustomerDtoFromCustomer(b.getCustomer())).build();
    }

    public Booking detailedBookingDtoToBooking(DetailedBookingDto b){
        return Booking.builder().id(b.getId()).startDate(b.getStartDate()).endDate(b.getEndDate())
                .extraBeds(b.getExtraBeds()).room(rr.findById(b.getMiniRoomDto().getId()).get())
                .customer(cr.findById(b.getMiniCustomerDto().getId()).get()).build();
    }

    private MiniCustomerDto createMiniCustomerDtoFromCustomer(Customer c){
        return new MiniCustomerDto(c.getId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhoneNumber());
    }

    public List<DetailedBookingDto> getAllDetailedBookings(){
        return br.findAll().stream().map(b -> bookingToDetailedBookingDto(b)).toList();
    }

    public List<MiniBookingDto> getAllMiniBookings(){
        return br.findAll().stream().map(b -> bookingToMiniBookingDto(b)).toList();
    }

    public DetailedBookingDto getDetailedBookingById(Long id){
        return bookingToDetailedBookingDto(br.findById(id).get());
    }
}
