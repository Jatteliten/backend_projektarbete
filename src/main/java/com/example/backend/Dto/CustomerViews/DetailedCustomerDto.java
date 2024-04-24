package com.example.backend.Dto.CustomerViews;

import com.example.backend.Dto.BookingViews.MiniBookingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedCustomerDto {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private MiniBookingDto miniBookingDto;
}
