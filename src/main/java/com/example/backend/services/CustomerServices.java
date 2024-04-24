package com.example.backend.services;

import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDtoForCustomer;
import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.repos.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServices {
    private final CustomerRepo cr;

    public CustomerServices(CustomerRepo cr){
        this.cr = cr;
    }

    public DetailedCustomerDto customerToDetailedCustomerDto(Customer c){
        return DetailedCustomerDto.builder().id(c.getId()).phoneNumber(c.getPhoneNumber()).firstName(c.getFirstName())
                .lastName(c.getLastName()).email(c.getEmail())
                .miniBookingDto(createMiniBookingDtoListFromBookingList(c.getBookings())).build();
    }

    public MiniCustomerDto customerToMiniCustomerDto(Customer c){
        return MiniCustomerDto.builder().id(c.getId()).firstName(c.getFirstName()).lastName(c.getLastName())
                .email(c.getEmail()).phoneNumber(c.getPhoneNumber()).build();
    }

    private List<MiniBookingDtoForCustomer> createMiniBookingDtoListFromBookingList(List<Booking> b){
        return b.stream().map(bb -> new MiniBookingDtoForCustomer(bb.getId(), bb.getStartDate(), bb.getEndDate(),
                new MiniRoomDto(bb.getRoom().getId(), bb.getRoom().getSize()))).collect(Collectors.toList());
    }

    public List<DetailedCustomerDto> getAllDetailedCustomers(){
        return cr.findAll().stream().map(c -> customerToDetailedCustomerDto(c)).toList();
    }

    public List<MiniCustomerDto> getAllMiniCustomers(){
        return cr.findAll().stream().map(c -> customerToMiniCustomerDto(c)).toList();
    }
}
