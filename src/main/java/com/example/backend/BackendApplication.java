package com.example.backend;

import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.repos.RoomRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Objects;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        if(args.length == 0) {
            SpringApplication.run(BackendApplication.class, args);
        } else if(Objects.equals(args[0], "fetchcontractcustomers")){
            SpringApplication application = new SpringApplication(FetchContractCustomers.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "fetchshippers")){
            SpringApplication application = new SpringApplication(FetchShippers.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        } else if(Objects.equals(args[0], "fetcheventqueue")){
            SpringApplication application = new SpringApplication(FetchEventQueue.class);
            application.setWebApplicationType(WebApplicationType.NONE);
            application.run(args);
        }
    }


    @Bean
    public CommandLineRunner commandLineRunner(RoomRepo roomRepo, BookingRepo br, CustomerRepo cr) {
        return (args -> {

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

            Booking b1 = new Booking(currentDay.plusDays(3),
                    currentDay.plusDays(6), 1, r1, c1);

            Booking b2 = new Booking(currentDay.plusMonths(1).plusDays(3),
                    currentDay.plusMonths(1).plusDays(5), 1, r1, c1);

            Booking b3 = new Booking(currentDay.plusDays(1),
                    currentDay.plusDays(6), 1, r4, c2);

            br.save(b1);
            br.save(b2);
            br.save(b3);

        });
    }



}