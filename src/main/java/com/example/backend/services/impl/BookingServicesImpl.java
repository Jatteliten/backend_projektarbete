package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.AddBookingView;
import com.example.backend.Dto.BookingViews.BookingSuccessView;
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
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    public String filterRooms(@Valid AddBookingView addBookingView, Model model){

        if (addBookingView.isStartPage()){
            List<Room> rooms = Collections.emptyList();
            model.addAttribute("listOfRooms",rooms);
            return "Booking/addBooking.html";
        }


        List<String> error = validate(addBookingView);
        if (!error.isEmpty()){
            List<Room> rooms = Collections.emptyList();
            model.addAttribute("listOfRooms",rooms);
            model.addAttribute("error",error);
            return "Booking/addBooking.html";
        }

        List<Room> occupiedRooms = br.findAll().stream()
                    .filter(b -> checkNotAvailable(b,addBookingView.getStartDate(),addBookingView.getEndDate()))
                    .map(b -> b.getRoom()).toList();

        List<Room> availableRooms = rr.findAll().stream().filter( room -> !occupiedRooms.contains(room)
        ).toList().stream().filter(room -> room.getSize() >= addBookingView.getBeds()+addBookingView.getExtraBeds()).toList();


        model.addAttribute("title","Available rooms");
        model.addAttribute("listOfRooms",availableRooms);
        model.addAttribute("buttonText","Book Room");
        model.addAttribute("start",addBookingView.getStartDate());
        model.addAttribute("end",addBookingView.getEndDate());
        model.addAttribute("error",error);
        return "Booking/addBooking.html";


    }

    private List<String> validate(AddBookingView addBookingView){
        List<String> error = new ArrayList<>();
        if (addBookingView.getStartDate().isBefore(LocalDate.now())){
            error.add("Cant book room before todays date");
        }
        if (addBookingView.getStartDate().isAfter(addBookingView.getEndDate())){
            error.add("Cant book room with start date before end date");
        }
        return error;
    }


    private boolean checkNotAvailable(Booking booking, LocalDate startDate, LocalDate endDate){

        LocalDate bookedStartDate = booking.getStartDate();
        LocalDate bookedEndDate = booking.getEndDate();

        boolean isStartWithin = !startDate.isBefore(bookedStartDate) && !startDate.isAfter(bookedEndDate);
        boolean isEndWithin = !endDate.isBefore(bookedStartDate) && !endDate.isAfter(bookedEndDate);

        boolean isBookedStartWithin = !bookedStartDate.isBefore(startDate) && !bookedStartDate.isAfter(endDate);
        boolean isBookedEndWithin = !bookedEndDate.isBefore(startDate) && !bookedEndDate.isAfter(endDate);

        return isStartWithin || isEndWithin || isBookedStartWithin || isBookedEndWithin;

    }

//    @Modifying
//    @Transactional
//    public void bookRoom(String email, Long roomId, LocalDate startDate, LocalDate endDate) {
//        Customer bookingCustomer = cr.findByEmail(email);
//        Room room = rr.findById(roomId).get();
//        bookingCustomer.addBooking(new Booking(startDate,endDate,calculateExtraBeds(room),room,bookingCustomer));
//        cr.save(bookingCustomer);
//    }
    @Modifying
    @Transactional
    public String bookRoom(BookingSuccessView bookingSuccessView, Model model) {
        Customer bookingCustomer = cr.findByEmail(bookingSuccessView.getEmail());
        Room room = rr.findById(bookingSuccessView.getRoomId()).get();
        bookingCustomer.addBooking(new Booking(bookingSuccessView.getStartDateB(),bookingSuccessView.getEndDateB(),calculateExtraBeds(room),room,bookingCustomer));
        cr.save(bookingCustomer);

        String error = null;
        model.addAttribute("error",error);
        model.addAttribute("email",bookingSuccessView.getEmail());
        model.addAttribute("roomId",bookingSuccessView.getRoomId());
        return "Booking/BookingSuccess.html";
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
