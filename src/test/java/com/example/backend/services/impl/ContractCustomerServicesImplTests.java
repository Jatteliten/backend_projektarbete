package com.example.backend.services.impl;

import com.example.backend.Dto.ContractCustomerViews.DetailedContractCustomer;
import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

class ContractCustomerServicesImplTests {
    ContractCustomer c1;
    @Mock
    ContractCustomerRepo mockContractCustomerRepo;
    @InjectMocks
    ContractCustomerServicesImpl customerServices;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        c1 = ContractCustomer.builder()
                .customerId(1L)
                .externalSystemId("1")
                .companyName("Hejbolaget")
                .contactName("John")
                .contactTitle("Arbetare")
                .streetAddress("Hejgatan 2")
                .city("Stockholm")
                .postalCode("11020")
                .country("Sweden")
                .phone("1112223344")
                .fax("1234567")
                .build();
    }

    @Test
    void contractCustomerToMiniContractCustomerDtoShouldReturnCorrectInformation() {
        MiniContractCustomerDto c2 = customerServices.contractCustomerToMiniContractCustomerDto(c1);

        Assertions.assertEquals(c2.getId(), c1.getCustomerId());
        Assertions.assertEquals(c2.getCountry(), c1.getCountry());
        Assertions.assertEquals(c2.getCompanyName(), c1.getCompanyName());
        Assertions.assertEquals(c2.getContactName(), c1.getContactName());
        Assertions.assertEquals(c2.getExternalSystemId(), c1.getExternalSystemId());
    }

    @Test
    void getSpecificContractCustomer() {
        when(mockContractCustomerRepo.findById(1L)).thenReturn(Optional.ofNullable(c1));

        Optional<ContractCustomer> c2 = mockContractCustomerRepo.findById(1L);

        Assertions.assertEquals(c1.getCompanyName(), c2.get().getCompanyName());
        Assertions.assertEquals(c1.getContactName(), c2.get().getContactName());
        Assertions.assertEquals(c1.getContactTitle(), c2.get().getContactTitle());
        Assertions.assertEquals(c1.getStreetAddress(), c2.get().getStreetAddress());
        Assertions.assertEquals(c1.getCity(), c2.get().getCity());
        Assertions.assertEquals(c1.getPostalCode(), c2.get().getPostalCode());
        Assertions.assertEquals(c1.getCountry(), c2.get().getCountry());
        Assertions.assertEquals(c1.getPhone(), c2.get().getPhone());
        Assertions.assertEquals(c1.getFax(), c2.get().getFax());
    }

    @Test
    void contractCustomerToDetailedContractCustomerDtoShouldReturnCorrectInformation() {
        DetailedContractCustomer c2 = customerServices.contractCustomerToDetailedContractCustomerDto(c1);

        Assertions.assertEquals(c1.getCompanyName(), c2.getCompanyName());
        Assertions.assertEquals(c1.getContactName(), c2.getContactName());
        Assertions.assertEquals(c1.getContactTitle(), c2.getContactTitle());
        Assertions.assertEquals(c1.getStreetAddress(), c2.getStreetAddress());
        Assertions.assertEquals(c1.getCity(), c2.getCity());
        Assertions.assertEquals(c1.getPostalCode(), c2.getPostalCode());
        Assertions.assertEquals(c1.getCountry(), c2.getCountry());
        Assertions.assertEquals(c1.getPhone(), c2.getPhone());
        Assertions.assertEquals(c1.getFax(), c2.getFax());
    }
}