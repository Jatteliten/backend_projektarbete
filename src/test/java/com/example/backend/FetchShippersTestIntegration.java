package com.example.backend;


import com.example.backend.repos.ShipperRepo;
import com.example.backend.services.XmlStreamProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class FetchShippersTestIntegration {
    @Autowired
    ShipperRepo shipperRepo;
    @Autowired
    XmlStreamProvider xmlStreamProvider;


    FetchShippers sut;

    @Test
    void fetchAndSaveShippersShouldSave() throws Exception {
        XmlStreamProvider xmlStreamProvider = mock(XmlStreamProvider.class);
        when(xmlStreamProvider.getShippersStream()).thenReturn(getClass().getClassLoader()
                .getResourceAsStream("shippers.json"));

        sut = new FetchShippers(shipperRepo, xmlStreamProvider);
        shipperRepo.deleteAll();
        sut.run();

        assertEquals(3, shipperRepo.count());
    }

    @Test
    void fetchAndSaveContractCustomersShouldSaveCorrectly() throws Exception {
        XmlStreamProvider xmlStreamProvider = mock(XmlStreamProvider.class);
        when(xmlStreamProvider.getShippersStream()).thenReturn(getClass().getClassLoader()
                .getResourceAsStream("shippers.json"));

        sut = new FetchShippers(shipperRepo, xmlStreamProvider);
        shipperRepo.deleteAll();
        sut.run();

        assertEquals(Long.valueOf(1L), shipperRepo.findById(1L).get().getId());
        assertEquals("birgitta.olsson@hotmail.com", shipperRepo.findById(1L).get().getEmail());
        assertEquals("Erik Östlund", shipperRepo.findById(1L).get().getContactName());
        assertEquals("painter", shipperRepo.findById(1L).get().getContactTitle());
        assertEquals("Järnvägsallén 955", shipperRepo.findById(1L).get().getStreetAddress());
        assertEquals("Gävhult", shipperRepo.findById(1L).get().getCity());
        assertEquals("07427", shipperRepo.findById(1L).get().getPostalCode());
        assertEquals("Sverige", shipperRepo.findById(1L).get().getCountry());
        assertEquals("070-569-3764", shipperRepo.findById(1L).get().getPhone());
        assertEquals("2634-25376", shipperRepo.findById(1L).get().getFax());
    }

    @Test
    void fetchContractCustomersWillFetch() throws IOException {
        sut = new FetchShippers(shipperRepo, xmlStreamProvider);
        Scanner s = new Scanner(sut.xmlStreamProvider.getShippersStream()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        assertTrue(  result.contains("[") );
        assertTrue(  result.contains("{") );
        assertTrue(  result.contains("id") );
        assertTrue(  result.contains("email") );
        assertTrue(  result.contains("companyName") );
        assertTrue(  result.contains("contactName") );
        assertTrue(  result.contains("contactTitle") );
        assertTrue(  result.contains("streetAddress") );
        assertTrue(  result.contains("city") );
        assertTrue(  result.contains("postalCode") );
        assertTrue(  result.contains("country") );
        assertTrue(  result.contains("phone") );
        assertTrue(  result.contains("fax") );
        assertTrue(  result.contains("}") );

    }

}
