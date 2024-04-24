package com.example.backend.Dto.BookingViews;

import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniBookingDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private MiniRoomDto miniRoomDto;
    private MiniCustomerDto miniCustomerDto;
}
