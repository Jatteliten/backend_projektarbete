package com.example.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.XmlStreamProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Set;

public class FetchContractCustomersTests {

    @Mock
    private ContractCustomerRepo contractCustomerRepo;

    @InjectMocks
    private FetchContractCustomers fetchContractCustomers;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAndApplyChangesToContractCustomer() {
        ContractCustomer newCustomer = new ContractCustomer();
        newCustomer.setCompanyName("New Company");
        newCustomer.setContactName("New Contact");
        newCustomer.setContactTitle("New Title");
        newCustomer.setStreetAddress("New Street");
        newCustomer.setCity("New City");
        newCustomer.setPostalCode("12345");
        newCustomer.setCountry("New Country");
        newCustomer.setPhone("123-456-7890");
        newCustomer.setFax("123-456-7891");

        ContractCustomer existingCustomer = new ContractCustomer();
        existingCustomer.setCompanyName("Old Company");

        Optional<ContractCustomer> optionalCustomer = Optional.of(existingCustomer);

        ContractCustomer updatedCustomer = fetchContractCustomers.findAndApplyChangesToContractCustomer(newCustomer, optionalCustomer);

        assertEquals("New Company", updatedCustomer.getCompanyName());
        assertEquals("New Contact", updatedCustomer.getContactName());
        assertEquals("New Title", updatedCustomer.getContactTitle());
        assertEquals("New Street", updatedCustomer.getStreetAddress());
        assertEquals("New City", updatedCustomer.getCity());
        assertEquals("12345", updatedCustomer.getPostalCode());
        assertEquals("New Country", updatedCustomer.getCountry());
        assertEquals("123-456-7890", updatedCustomer.getPhone());
        assertEquals("123-456-7891", updatedCustomer.getFax());
    }

    @Test
    public void testValidateContractCustomer() {

        ContractCustomer validCustomer = new ContractCustomer();
        validCustomer.setExternalSystemId("12345");
        validCustomer.setCompanyName("Valid Company");
        validCustomer.setContactName("Valid Contact");
        validCustomer.setContactTitle("Valid Title");
        validCustomer.setStreetAddress("Valid Street");
        validCustomer.setCity("Valid City");
        validCustomer.setPostalCode("12345");
        validCustomer.setCountry("Valid Country");
        validCustomer.setPhone("123-456-7890");
        validCustomer.setFax("123-456-7891");

        assertDoesNotThrow(() -> fetchContractCustomers.validateContractCustomer(validCustomer));

        ContractCustomer invalidCustomer = new ContractCustomer();
        invalidCustomer.setExternalSystemId("12345");
        invalidCustomer.setCompanyName("Valid Company");
        invalidCustomer.setContactName("Valid Contact");
        invalidCustomer.setContactTitle("Valid Title");
        invalidCustomer.setStreetAddress("Valid Street");
        invalidCustomer.setCity("Valid City");
        invalidCustomer.setPostalCode("Invalid postal code");
        invalidCustomer.setCountry("Valid Country");
        invalidCustomer.setPhone("123-456-7890");
        invalidCustomer.setFax("123-456-7891");

        InputMismatchException exception = assertThrows(InputMismatchException.class, () -> {
            fetchContractCustomers.validateContractCustomer(invalidCustomer);
        });

        assertTrue(exception.getMessage().contains("Wrong postal code format"));

    }

}
