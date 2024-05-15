package com.example.backend;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

@ComponentScan
public class BeanInitialisation implements CommandLineRunner {
    RoomRepo rr;
    BookingRepo br;
    CustomerRepo cr;

    public BeanInitialisation(RoomRepo rr, BookingRepo br, CustomerRepo cr){
        this.rr = rr;
        this.br = br;
        this.cr = cr;
    }

    @Override
    public void run(String... args){

        Room r1 = Room.builder().size(1).pricePerNight(100).build();
        Room r2 = Room.builder().size(1).pricePerNight(100).build();
        Room r3 = Room.builder().size(1).pricePerNight(100).build();
        Room r4 = Room.builder().size(1).pricePerNight(100).build();
        Room r5 = Room.builder().size(2).pricePerNight(200).build();
        Room r6 = Room.builder().size(2).pricePerNight(200).build();
        Room r7 = Room.builder().size(3).pricePerNight(300).build();
        Room r8 = Room.builder().size(3).pricePerNight(300).build();
        Room r9 = Room.builder().size(4).pricePerNight(400).build();
        Room r10 = Room.builder().size(4).pricePerNight(400).build();

        rr.save(r1);
        rr.save(r2);
        rr.save(r3);
        rr.save(r4);
        rr.save(r5);
        rr.save(r6);
        rr.save(r7);
        rr.save(r8);
        rr.save(r9);
        rr.save(r10);

        Customer c1 = Customer.builder()
                .firstName("Daniel")
                .lastName("Isaksson")
                .email("Daniel@hej.se")
                .phoneNumber("0755232222")
                .build();
        Customer c2 = Customer.builder()
                .firstName("Lise")
                .lastName("Martinsen")
                .email("Lise@hej.se")
                .phoneNumber("0732511663")
                .build();
        Customer c3 = Customer.builder()
                .firstName("Martin")
                .lastName("Harrysson")
                .email("Martin@hej.se")
                .phoneNumber("0725523223")
                .build();
        Customer c4 = Customer.builder()
                .firstName("Astrid")
                .lastName("Rosen")
                .email("Astrid@hej.se")
                .phoneNumber("0732511655")
                .build();

        cr.save(c1);
        cr.save(c2);
        cr.save(c3);
        cr.save(c4);

        LocalDate currentDay = LocalDate.now();

        Booking b1 = Booking.builder()
                .startDate(currentDay.plusDays(3))
                .endDate(currentDay.plusDays(6))
                .extraBeds(1)
                .room(r1)
                .customer(c1)
                .build();

        Booking b2 = Booking.builder()
                .startDate(currentDay.plusMonths(1).plusDays(3))
                .endDate(currentDay.plusMonths(1).plusDays(5))
                .extraBeds(1)
                .room(r1)
                .customer(c1)
                .build();

        Booking b3 = Booking.builder()
                .startDate(currentDay.plusDays(1))
                .endDate(currentDay.plusDays(6))
                .extraBeds(1)
                .room(r4)
                .customer(c2)
                .build();

        br.save(b1);
        br.save(b2);
        br.save(b3);
    }
}
