package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.BookingViews.MiniBookingDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import com.example.backend.services.BookingServices;
import com.example.backend.services.CustomerServices;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingServicesImpl implements BookingServices {
    private final BookingRepo br;
    private final CustomerRepo cr;
    private final CustomerServices cs;
    private final RoomRepo rr;
    private final Validator validator;

    public BookingServicesImpl(BookingRepo br, CustomerRepo cr, RoomRepo rr, CustomerServices cs) {
        this.br = br;
        this.cr = cr;
        this.rr = rr;
        this.cs = new CustomerServicesImpl(cr, br);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public DetailedBookingDto bookingToDetailedBookingDto(Booking b) {
        return DetailedBookingDto.builder()
                .id(b.getId())
                .extraBeds(b.getExtraBeds())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .miniRoomDto(MiniRoomDto.builder()
                        .id(b.getRoom().getId())
                        .size(b.getRoom().getSize())
                        .pricePerNight(b.getRoom().getPricePerNight())
                        .build())
                .miniCustomerDto(cs.customerToMiniCustomerDto(b.getCustomer()))
                .build();
    }

    public MiniBookingDto bookingToMiniBookingDto(Booking b) {
        return MiniBookingDto.builder()
                .id(b.getId())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .miniRoomDto(MiniRoomDto.builder()
                        .id(b.getRoom().getId())
                        .size(b.getRoom().getSize())
                        .pricePerNight(b.getRoom().getPricePerNight())
                        .build())
                .miniCustomerDto(cs.customerToMiniCustomerDto(b.getCustomer()))
                .build();
    }

    public Booking detailedBookingDtoToBooking(DetailedBookingDto b) {
        return Booking.builder()
                .id(b.getId())
                .startDate(b.getStartDate())
                .endDate(b.getEndDate())
                .extraBeds(b.getExtraBeds())
                .room(rr.findById(b.getMiniRoomDto().getId()).get())
                .customer(cr.findById(b.getMiniCustomerDto().getId()).get())
                .build();
    }


    public List<DetailedBookingDto> getAllDetailedBookings() {
        return br.findAll().stream().map(b -> bookingToDetailedBookingDto(b)).toList();
    }

    public List<MiniBookingDto> getAllMiniBookings() {
        return br.findAll().stream().map(b -> bookingToMiniBookingDto(b)).toList();
    }

    public DetailedBookingDto getDetailedBookingById(Long id) {
        return bookingToDetailedBookingDto(br.findById(id).get());
    }

    @Override
    public void addBooking(Booking b) {
        br.save(b);
    }

    @Override
    public void deleteBooking(Booking b) {
        br.delete(b);
    }

    @Override
    public List<MiniBookingDto> findBookings(String searchWord) {
        List<MiniBookingDto> bookingMatches = new ArrayList<>();
        searchWord = searchWord.toLowerCase();
        List<MiniBookingDto> allBookings = getAllMiniBookings();
        for (MiniBookingDto b : allBookings) {
            if (
                    b.getId().toString().contains(searchWord) ||
                            b.getStartDate().toString().contains(searchWord) ||
                            b.getEndDate().toString().contains(searchWord) ||
                            b.getMiniRoomDto().getId().toString().contains(searchWord) ||
                            b.getMiniCustomerDto().getId().toString().contains(searchWord) ||
                            b.getMiniCustomerDto().getEmail().toLowerCase().contains(searchWord) ||
                            b.getMiniCustomerDto() .getFirstName().toLowerCase().contains(searchWord) ||
                            b.getMiniCustomerDto() .getLastName().toLowerCase().contains(searchWord) ||
                            b.getMiniCustomerDto().getPhoneNumber().toLowerCase().contains(searchWord)
            ) {
                bookingMatches.add(b);
            }
        }
        return bookingMatches;
    }

    public MiniBookingDto getMiniBookingById(Long id) {
        Booking booking = br.findById(id).get();
        return bookingToMiniBookingDto(booking);
    }

    public void deleteBookingById(Long id) {
        Booking booking = br.findById(id).get();
        br.delete(booking);
    }

    public String updateBooking(Long bookingId, LocalDate startDate, LocalDate endDate, Long roomId,int extraBeds) {
        Booking booking = br.findById(bookingId).get();
        Booking validateBooking = Booking.builder()
                .startDate(startDate)
                .endDate(endDate)
                .extraBeds(extraBeds)
                .build();
        Room room = rr.findById(roomId).get();

        Set<ConstraintViolation<Booking>> violations = validator.validate(validateBooking);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Booking> violation : violations) {
                errorMessages.append(" - ").append(violation.getMessage());
            }
            return String.valueOf(errorMessages);
        }
        else {
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setRoom(room);
            booking.setExtraBeds(extraBeds);
            br.save(booking);
            return "Success!";
        }
    }

    public List<Room> filterRooms(Integer beds, Integer extraBeds, LocalDate startDate, LocalDate endDate) {
        if (beds == null || startDate == null || endDate == null) return Collections.emptyList();

        List<Room> occupiedRooms = br.findAll().stream()
                .filter(b -> checkNotAvailable(b, startDate, endDate))
                .map(b -> b.getRoom()).toList();

        List<Room> availableRooms = rr.findAll().stream().filter(room -> !occupiedRooms.contains(room)
        ).toList();


        return availableRooms.stream().filter(room -> room.getSize() >= beds + extraBeds).toList();
    }

    public boolean checkNotAvailable(Booking booking, LocalDate startDate, LocalDate endDate) {

        LocalDate bookedStartDate = booking.getStartDate();
        LocalDate bookedEndDate = booking.getEndDate();

        boolean isStartWithin = !startDate.isBefore(bookedStartDate) && !startDate.isAfter(bookedEndDate);
        boolean isEndWithin = !endDate.isBefore(bookedStartDate) && !endDate.isAfter(bookedEndDate);

        boolean isBookedStartWithin = !bookedStartDate.isBefore(startDate) && !bookedStartDate.isAfter(endDate);
        boolean isBookedEndWithin = !bookedEndDate.isBefore(startDate) && !bookedEndDate.isAfter(endDate);

        return isStartWithin || isEndWithin || isBookedStartWithin || isBookedEndWithin;

    }

    @Modifying
    @Transactional
    public String bookRoom(String email, Long roomId, LocalDate startDate, LocalDate endDate) {
        Customer bookingCustomer = cr.findByEmail(email);
        Room room = rr.findById(roomId).get();
        Booking newBooking = Booking.builder().
                startDate(startDate).
                endDate(endDate).
                extraBeds(calculateExtraBeds(room)).
                room(room).
                customer(bookingCustomer).
                build();

        Set<ConstraintViolation<Booking>> violations = validator.validate(newBooking);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Booking> violation : violations) {
                errorMessages.append(" - ").append(violation.getMessage());
            }
            return String.valueOf(errorMessages);
        }
        else {
            bookingCustomer.addBooking(newBooking);
            cr.save(bookingCustomer);
            return "Success!";
        }
    }

    public int calculateExtraBeds(Room room) {
        return switch (room.getSize()) {
            case 1 -> 0;
            case 2 -> 0;
            case 3 -> 1;
            case 4 -> 2;
            default -> 0;
        };

    }

    public Booking findById(Long id) {
        return br.findById(id).get();
    }
}
