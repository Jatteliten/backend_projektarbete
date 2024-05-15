package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @Future(message = "Start date must be in the future")
    private LocalDate startDate;
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @PositiveOrZero(message = "Cant be a negative number")
    @Max(value = 2, message = "Maximum of two extra beds")
    private int extraBeds;

    @ManyToOne
    private Room room;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
