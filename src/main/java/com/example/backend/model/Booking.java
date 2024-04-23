package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private int extraBeds;

    @ManyToOne
    private Room room;

    public Booking(LocalDate startDate, LocalDate endDate, int extraBeds, Room room){
        this.startDate = startDate;
        this.endDate = endDate;
        this.extraBeds = extraBeds;
        this.room = room;
    }

}
