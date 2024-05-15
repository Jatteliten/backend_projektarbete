package com.example.backend;

import static org.junit.jupiter.api.Assertions.*;

import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class FetchContractCustomersTest {

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
}
