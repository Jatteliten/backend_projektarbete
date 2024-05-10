package com.example.backend.Dto.ShipperViews;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniShipperDto {
    private Long id;
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
    private String country;
}
