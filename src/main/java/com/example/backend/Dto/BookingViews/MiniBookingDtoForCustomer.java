package com.example.backend.Dto.BookingViews;

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
public class MiniBookingDtoForCustomer {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private MiniRoomDto miniRoomDto;
}
