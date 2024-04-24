package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.MiniBookingDtoForCustomer;
import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.services.CustomerServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServicesImpl implements CustomerServices {
    private final CustomerRepo cr;
    private final BookingRepo br;

    public CustomerServicesImpl(CustomerRepo cr, BookingRepo br){
        this.cr = cr;
        this.br = br;
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

    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c){
        return Customer.builder().id(c.getId()).firstName(c.getFirstName()).lastName(c.getLastName())
                .email(c.getEmail()).phoneNumber(c.getPhoneNumber())
                .bookings(createBookingListFromMiniBookingsDtoList(c.getMiniBookingDto())).build();
    }

    private List<Booking> createBookingListFromMiniBookingsDtoList(List<MiniBookingDtoForCustomer> b){
        return b.stream().map(bb -> br.findById(bb.getId()).get()).toList();
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

    public DetailedCustomerDto getDetailedCustomerById(Long id){
        return customerToDetailedCustomerDto(cr.findById(id).get());
    }

    @Override
    public void addCustomer(DetailedCustomerDto detailedCustomerDto) {
        cr.save(detailedCustomerDtoToCustomer(detailedCustomerDto));
    }

    @Override
    public void deleteCustomer(DetailedCustomerDto detailedCustomerDto) {
        Optional<Customer> optionalCustomer = cr.findById(detailedCustomerDto.getId());
        if (optionalCustomer.isPresent()) {
            cr.deleteById(detailedCustomerDto.getId());
        } else {
            //lägg till ev felmeddelande
        }
    }
}
