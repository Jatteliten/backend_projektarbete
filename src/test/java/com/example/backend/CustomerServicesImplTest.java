package com.example.backend;


import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.services.impl.CustomerServicesImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerServicesImplTest {

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private BookingRepo bookingRepo;

    //@InjectMocks
    private CustomerServicesImpl services = new CustomerServicesImpl(customerRepo, bookingRepo);

    Long customerId = 1L;
    String firstName = "Lise";
    String lastName = "Martinsen";
    String email = "lise.martinsen@yh.nackademin.se";
    String phoneNumber = "071234567";

    Customer customer = new Customer(customerId, firstName, lastName, email, phoneNumber, null);
    MiniCustomerDto miniCustomer = new MiniCustomerDto(customerId, firstName, lastName, email, phoneNumber);

    Long bookingId = 1L;

    Booking booking;

}
