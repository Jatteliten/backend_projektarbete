package com.example.backend;

import com.example.backend.model.modelUti.ShipperDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.*;

public class FetchShippersTests {

    @InjectMocks
    private FetchShippers fetchShippers;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateShippers() {
        ShipperDto validShipper = new ShipperDto();
        validShipper.setId(12345);
        validShipper.setEmail("valid.email@gmail.com");
        validShipper.setCompanyName("Valid Company");
        validShipper.setContactName("Valid Contact");
        validShipper.setContactTitle("Valid Title");
        validShipper.setStreetAddress("Valid Street");
        validShipper.setCity("Valid City");
        validShipper.setPostalCode("12345");
        validShipper.setCountry("Valid Country");
        validShipper.setPhone("123-456-7890");
        validShipper.setFax("123-456-7891");


        assertDoesNotThrow(() -> fetchShippers.validateShippers(validShipper));

        ShipperDto invalidShipper = new ShipperDto();
        invalidShipper.setId(12345);
        invalidShipper.setEmail("valid.email@gmail.com");
        invalidShipper.setCompanyName(null);
        invalidShipper.setContactName("Valid Contact");
        invalidShipper.setContactTitle("Valid Title");
        invalidShipper.setStreetAddress("Valid Street");
        invalidShipper.setCity("Valid City");
        invalidShipper.setPostalCode("12345");
        invalidShipper.setCountry("Valid Country");
        invalidShipper.setPhone("123-456-7890");
        invalidShipper.setFax("123-456-7891");

        InputMismatchException exception = assertThrows(InputMismatchException.class, () -> {
            fetchShippers.validateShippers(invalidShipper);
        });

        assertTrue(exception.getMessage().contains("Company name is empty"));

    }
}
