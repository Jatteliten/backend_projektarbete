package com.example.backend;

import com.example.backend.model.Room;
import com.example.backend.repos.RoomRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(RoomRepo roomRepo) {
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

        });
    }
}
