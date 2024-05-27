package com.example.backend.services.impl;

import com.example.backend.Dto.ShipperViews.MiniShipperDto;
import com.example.backend.model.Shipper;
import com.example.backend.repos.ShipperRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void shipperToMiniShipperDtoShouldReturnCorrectInformation() {
        MiniShipperDto miniS = shipperServicesImpl.shipperToMiniShipperDto(s1);

        assertEquals(s1.getId(), miniS.getId());
        assertEquals(s1.getCompanyName(), miniS.getCompanyName());
        assertEquals(s1.getContactName(), miniS.getContactName());
        assertEquals(s1.getEmail(), miniS.getEmail());
        assertEquals(s1.getPhone(), miniS.getPhone());
        assertEquals(s1.getCountry(), miniS.getCountry());
    }

    @Test
    void getAllMiniShippersDtoShouldReturnCorrectAmountOfMiniDTOs() {
        when(shipperRepo.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<MiniShipperDto> list = shipperServicesImpl.getAllMiniShippersDto();
        assertEquals(2, list.size());
    }

    @Test
    void getAllMiniShippersDtoShouldReturnCorrectInformation() {
        when(shipperRepo.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<MiniShipperDto> list = shipperServicesImpl.getAllMiniShippersDto();
        assertEquals(list.get(0).getId(), s1.getId());
        assertEquals(list.get(0).getEmail(), s1.getEmail());
        assertEquals(list.get(0).getCompanyName(), s1.getCompanyName());
        assertEquals(list.get(0).getContactName(), s1.getContactName());
        assertEquals(list.get(0).getPhone(), s1.getPhone());
        assertEquals(list.get(0).getCountry(), s1.getCountry());
        assertEquals(list.get(1).getId(), s2.getId());
        assertEquals(list.get(1).getEmail(), s2.getEmail());
        assertEquals(list.get(1).getCompanyName(), s2.getCompanyName());
        assertEquals(list.get(1).getContactName(), s2.getContactName());
        assertEquals(list.get(1).getPhone(), s2.getPhone());
        assertEquals(list.get(1).getCountry(), s2.getCountry());
    }
}