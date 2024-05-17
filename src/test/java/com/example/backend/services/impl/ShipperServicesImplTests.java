package com.example.backend.services.impl;

import com.example.backend.Dto.ShipperViews.MiniShipperDto;
import com.example.backend.model.Shipper;
import com.example.backend.repos.ShipperRepo;
import com.example.backend.services.ShipperServices;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ShipperServicesImplTests {
    @Mock
    private ShipperRepo shipperRepo;
    @InjectMocks
    private ShipperServicesImpl shipperServicesImpl;
    Shipper s1 = Shipper.builder()
            .externalId(1)
            .email("hej@hej.se")
            .companyName("Hej AB")
            .contactName("Daniel")
            .streetAddress("Hejgatan 2")
            .city("Stockholm")
            .postalCode("12345")
            .country("Sweden")
            .phone("123456789")
            .fax("1234567")
            .build();

    Shipper s2 = Shipper.builder()
            .externalId(1)
            .email("hejdå@hejdå.se")
            .companyName("Hej Då AB")
            .contactName("Sarah")
            .streetAddress("Hejdågatan 2")
            .city("Stockholm")
            .postalCode("11111")
            .country("Sweden")
            .phone("111111111")
            .fax("1111111")
            .build();

    @Test
    void shipperToMiniShipperDto() {
        MiniShipperDto miniS = shipperServicesImpl.shipperToMiniShipperDto(s1);

        assertEquals(s1.getId(), miniS.getId());
        assertEquals(s1.getCompanyName(), miniS.getCompanyName());
        assertEquals(s1.getContactName(), miniS.getContactName());
        assertEquals(s1.getEmail(), miniS.getEmail());
        assertEquals(s1.getPhone(), miniS.getPhone());
        assertEquals(s1.getCountry(), miniS.getCountry());
    }

    @Test
    void getAllMiniShippersDto() {
        when(shipperRepo.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<MiniShipperDto> list = shipperServicesImpl.getAllMiniShippersDto();
        assertEquals(2, list.size());
    }
}