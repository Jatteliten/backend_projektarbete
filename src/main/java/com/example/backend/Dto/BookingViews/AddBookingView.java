package com.example.backend.Dto.BookingViews;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBookingView {
    @Future(message = "Start date must be in the future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Min(value = 1, message = "Must be minimum one bed")
    @Max(value = 2, message = "Must be maximum two beds")
    private Integer beds;

    @Min(value = 0, message = "Must be a positive number")
    @Max(value = 2, message = "Must be maximum two beds")
    private Integer extraBeds;

    private boolean startPage = false;

    public AddBookingView(LocalDate startDate, LocalDate endDate, int beds, int extraBeds) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.beds = beds;
        this.extraBeds = extraBeds;
    }

    public AddBookingView(Boolean startPage) {
        this.startPage = startPage;
    }
}
