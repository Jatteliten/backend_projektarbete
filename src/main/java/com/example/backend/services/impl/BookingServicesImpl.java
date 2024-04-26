package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import com.example.backend.services.BookingServices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServicesImpl implements BookingServices {
    private final BookingRepo br;
    private final CustomerRepo cr;
    private final RoomRepo rr;

    public BookingServicesImpl(BookingRepo br, CustomerRepo cr, RoomRepo rr){
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

    @Override
    public void addBooking(Booking b) {
        br.save(b);
    }

    @Override
    public void deleteBooking(Booking b) {
        if (b != null) {
            br.delete(b);
        } else {
            //lägg till ev felmeddelande
        }
    }

    public List<Room> filterRooms(Integer beds, Integer extraBeds, LocalDate startDate, LocalDate endDate){
        if (beds == null || startDate== null || endDate == null  ) return rr.findAll();


        List<Room> occupiedRooms = br.findAll().stream()
                .filter(b -> checkNotAvailable(b,startDate,endDate))
                .map(b -> b.getRoom()).toList();

        List<Room> availableRooms = rr.findAll().stream().filter( room -> !occupiedRooms.contains(room)
        ).toList();


        return availableRooms.stream().filter(room -> room.getSize() >= beds+extraBeds).toList();
    }

    private boolean checkNotAvailable(Booking booking, LocalDate startDate, LocalDate endDate){
        if ((startDate.isBefore(booking.getEndDate()) || startDate.equals(booking.getEndDate())) &&
                (endDate.isAfter(booking.getStartDate()) || endDate.equals(booking.getStartDate()))) {
            return true;
        }
        return false;
    }

    public void bookRoom(String email, Long roomId, LocalDate startDate, LocalDate endDate) {
        Customer bookingCustomer = cr.findByEmail(email);
        Room room = rr.findById(roomId).get();

        br.save(new Booking(startDate,endDate,calculateExtraBeds(room),room,bookingCustomer));

    }
    private int calculateExtraBeds(Room room){
        return switch (room.getSize()){
            case 1 -> 0;
            case 2 -> 0;
            case 3 -> 1;
            case 4 -> 2;
            default -> 0;
        };

    }

    public Booking findById(Long id){
        return br.findById(id).get();
    }
}
