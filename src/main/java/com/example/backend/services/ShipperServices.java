package com.example.backend.services;

import com.example.backend.Dto.ShipperViews.MiniShipperDto;
import com.example.backend.model.Shipper;

import java.util.List;

public interface ShipperServices {
    MiniShipperDto shipperToMiniShipperDto(Shipper s);
    List<MiniShipperDto> getAllMiniShippersDto();
}
