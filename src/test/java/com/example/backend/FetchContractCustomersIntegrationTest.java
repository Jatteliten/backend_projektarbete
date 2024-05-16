package com.example.backend;

import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.XmlStreamProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class FetchContractCustomersIntegrationTest {
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

        assertEquals("1", contractCustomerRepo.findByExternalSystemId("1").getExternalSystemId());
        assertEquals("Persson Kommanditbolag", contractCustomerRepo.findByExternalSystemId("1").getCompanyName());
        assertEquals("Maria Åslund", contractCustomerRepo.findByExternalSystemId("1").getContactName());
        assertEquals("gardener", contractCustomerRepo.findByExternalSystemId("1").getContactTitle());
        assertEquals("Anderssons Gata 259", contractCustomerRepo.findByExternalSystemId("1").getStreetAddress());
        assertEquals("Kramland", contractCustomerRepo.findByExternalSystemId("1").getCity());
        assertEquals("60843", contractCustomerRepo.findByExternalSystemId("1").getPostalCode());
        assertEquals("Sverige", contractCustomerRepo.findByExternalSystemId("1").getCountry());
        assertEquals("076-340-7143", contractCustomerRepo.findByExternalSystemId("1").getPhone());
        assertEquals("1500-16026", contractCustomerRepo.findByExternalSystemId("1").getFax());
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