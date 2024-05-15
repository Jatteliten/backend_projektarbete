package com.example.backend.Dto.RoomViews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniRoomDto {
    private Long id;
    private int size;
    private double pricePerNight;
}
