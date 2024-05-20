package com.example.backend;

import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.XmlStreamProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FetchContractCustomersTestIntegration {
    @Autowired
    ContractCustomerRepo contractCustomerRepo;
    @Autowired
    XmlStreamProvider xmlStreamProvider;

    FetchContractCustomers sut;

    @Test
    void fetchAndSaveContractCustomersShouldSave() throws Exception {
        XmlStreamProvider xmlStreamProvider = mock(XmlStreamProvider.class);
        when(xmlStreamProvider.getContractCustomersStream()).thenReturn(getClass().getClassLoader()
                .getResourceAsStream("contractCustomers.xml"));

        sut = new FetchContractCustomers(contractCustomerRepo, xmlStreamProvider);
        contractCustomerRepo.deleteAll();
        sut.run();

        assertEquals(3, contractCustomerRepo.count());
    }

    @Test
    void fetchAndSaveContractCustomersShouldSaveCorrectly() throws Exception {
        XmlStreamProvider xmlStreamProvider = mock(XmlStreamProvider.class);
        when(xmlStreamProvider.getContractCustomersStream()).thenReturn(getClass().getClassLoader()
                .getResourceAsStream("contractCustomers.xml"));

        sut = new FetchContractCustomers(contractCustomerRepo, xmlStreamProvider);
        contractCustomerRepo.deleteAll();
        sut.run();

        assertEquals("1", contractCustomerRepo.findById(1L).get().getExternalSystemId());
        assertEquals("Persson Kommanditbolag", contractCustomerRepo.findById(1L).get().getCompanyName());
        assertEquals("Maria Åslund", contractCustomerRepo.findById(1L).get().getContactName());
        assertEquals("gardener", contractCustomerRepo.findById(1L).get().getContactTitle());
        assertEquals("Anderssons Gata 259", contractCustomerRepo.findById(1L).get().getStreetAddress());
        assertEquals("Kramland", contractCustomerRepo.findById(1L).get().getCity());
        assertEquals("60843", contractCustomerRepo.findById(1L).get().getPostalCode());
        assertEquals("Sverige", contractCustomerRepo.findById(1L).get().getCountry());
        assertEquals("076-340-7143", contractCustomerRepo.findById(1L).get().getPhone());
        assertEquals("1500-16026", contractCustomerRepo.findById(1L).get().getFax());
    }


    @Test
    void fetchContractCustomersWillFetch() throws IOException {
        sut = new FetchContractCustomers(contractCustomerRepo, xmlStreamProvider);
        Scanner s = new Scanner(sut.xmlStreamProvider.getContractCustomersStream()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        assertTrue(  result.contains("<allcustomers>") );
        assertTrue(  result.contains("</allcustomers>") );
        assertTrue(  result.contains("<customers>") );
        assertTrue(  result.contains("</customers>") );
        assertTrue(  result.contains("<id>") );
        assertTrue(  result.contains("</id>") );
        assertTrue(  result.contains("<companyName>") );
        assertTrue(  result.contains("</companyName>") );
        assertTrue(  result.contains("<contactName>") );
        assertTrue(  result.contains("</contactName>") );
        assertTrue(  result.contains("<contactTitle>") );
        assertTrue(  result.contains("</contactTitle>") );
        assertTrue(  result.contains("<streetAddress>") );
        assertTrue(  result.contains("</streetAddress>") );
        assertTrue(  result.contains("<city>") );
        assertTrue(  result.contains("</city>") );
        assertTrue(  result.contains("<postalCode>") );
        assertTrue(  result.contains("</postalCode>") );
        assertTrue(  result.contains("<country>") );
        assertTrue(  result.contains("</country>") );
        assertTrue(  result.contains("<phone>") );
        assertTrue(  result.contains("</phone>") );
        assertTrue(  result.contains("<fax>") );
        assertTrue(  result.contains("</fax>") );
    }
}