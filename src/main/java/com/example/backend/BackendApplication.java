package com.example.backend;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoomRepo roomRepo, BookingRepo br, CustomerRepo cr) {
        return (args -> {
            Customer c1 = new Customer("Daniel", "Isaksson", "Daniel@hej.se", "0725523222");
            Customer c2 = new Customer("Lise", "Martinsen", "Lise@hej.se", "0732511663");

            cr.save(c1);
            cr.save(c2);
        });
    }

    /*
    @Bean
    public CommandLineRunner commandLineRunner(RoomRepo roomRepo, BookingRepo br, CustomerRepo cr) {
        return (args -> {

            Room r1 = new Room(1);
            Room r2 = new Room(1);
            Room r3 = new Room(1);
            Room r4 = new Room(1);
            Room r5 = new Room(2);
            Room r6 = new Room(2);
            Room r7 = new Room(3);
            Room r8 = new Room(3);
            Room r9 = new Room(4);
            Room r10 = new Room(4);

            roomRepo.save(r1);
            roomRepo.save(r2);
            roomRepo.save(r3);
            roomRepo.save(r4);
            roomRepo.save(r5);
            roomRepo.save(r6);
            roomRepo.save(r7);
            roomRepo.save(r8);
            roomRepo.save(r9);
            roomRepo.save(r10);

            Customer c1 = new Customer("Daniel", "Isaksson", "Daniel@hej.se", "0725523222");
            Customer c2 = new Customer("Lise", "Martinsen", "Lise@hej.se", "0732511663");

            cr.save(c1);
            cr.save(c2);

            Booking b1 = new Booking(LocalDate.of(2022, 10, 10),
                    LocalDate.of(2022, 10, 12), 1, r1, c1);

            Booking b2 = new Booking(LocalDate.of(2022, 11, 11),
                    LocalDate.of(2022, 11, 16), 1, r1, c1);

            Booking b3 = new Booking(LocalDate.of(2022, 11, 11),
                    LocalDate.of(2022, 11, 14), 1, r4, c2);

            br.save(b1);
            br.save(b2);
            br.save(b3);

        });
    }

     */
}
