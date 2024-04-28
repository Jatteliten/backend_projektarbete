package com.example.backend.Dto.BookingViews;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSuccessView {

    @Email
    private String email;
    private Long roomId;
    @DateTimeFormat
    private LocalDate startDateB;
    @DateTimeFormat
    private LocalDate endDateB;
}
