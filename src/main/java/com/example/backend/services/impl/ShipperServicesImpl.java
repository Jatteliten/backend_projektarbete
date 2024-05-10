package com.example.backend.services.impl;

import com.example.backend.Dto.ShipperViews.MiniShipperDto;
import com.example.backend.model.Shipper;
import com.example.backend.repos.ShipperRepo;
import com.example.backend.services.ShipperServices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperServicesImpl implements ShipperServices {
    private final ShipperRepo sr;

    public ShipperServicesImpl(ShipperRepo sr){
        this.sr = sr;
    }

    @Override
    public MiniShipperDto shipperToMiniShipperDto(Shipper s){
        return MiniShipperDto.builder().id(s.getId()).companyName(s.getCompanyName()).contactName(s.getContactName())
                .email(s.getEmail()).phone(s.getPhone()).country(s.getCountry()).build();
    }

    @Override
    public List<MiniShipperDto> getAllMiniShippersDto(){
        return sr.findAll().stream().map(s -> shipperToMiniShipperDto(s)).toList();
    }
}
