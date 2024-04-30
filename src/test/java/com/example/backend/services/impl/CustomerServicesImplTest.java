package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.MiniBookingDtoForCustomer;
import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.model.Room;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;


class CustomerServicesImplTest {

    Customer c;
    List<Booking> b;
    MiniCustomerDto mc;
    DetailedCustomerDto dc;

    @Mock
    private CustomerRepo mockCustomerRepo;

    @Mock
    private BookingRepo mockBookingRepo;

    @InjectMocks
    private CustomerServicesImpl cs;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        c = new Customer(1L, "Daniel", "Isaksson",
                "Daniel@hej.com", "0722055577");
        b = List.of(new Booking(1L, LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 10, 12), 1, new Room(1L, 3), c));
        c.setBookings(b);

        dc = cs.customerToDetailedCustomerDto(c);
        mc = cs.customerToMiniCustomerDto(c);
        System.out.println(dc.getMiniBookingDto().get(0).getId());

        when(mockCustomerRepo.findById(1L)).thenReturn(Optional.of(c));
        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(b.get(0)));
    }

    @Test
    void customerToDetailedCustomerDto() {
        assertEquals(c.getId(), dc.getId());
        assertEquals(c.getFirstName(), dc.getFirstName());
        assertEquals(c.getLastName(), dc.getLastName());
        assertEquals(c.getEmail(), dc.getEmail());
        assertEquals(c.getPhoneNumber(), dc.getPhoneNumber());
        assertEquals(1, dc.getMiniBookingDto().size());
    }

    @Test
    void customerToMiniCustomerDto() {
        assertEquals(c.getId(), mc.getId());
        assertEquals(c.getFirstName(), mc.getFirstName());
        assertEquals(c.getLastName(), mc.getLastName());
        assertEquals(c.getEmail(), mc.getEmail());
        assertEquals(c.getPhoneNumber(), mc.getPhoneNumber());
    }

    @Test
    void miniCustomerDtoToCustomer() {
        Customer c2 = cs.miniCustomerDtoToCustomer(mc);

        assertEquals(c2.getId(), mc.getId());
        assertEquals(c2.getFirstName(), mc.getFirstName());
        assertEquals(c2.getLastName(), mc.getLastName());
        assertEquals(c2.getEmail(), mc.getEmail());
        assertEquals(c2.getPhoneNumber(), mc.getPhoneNumber());
    }

    @Test
    void detailedCustomerDtoToCustomer() {
        Customer c2 = cs.detailedCustomerDtoToCustomer(dc);

        System.out.println(c2.getBookings().get(0).getId());
        System.out.println(dc.getMiniBookingDto().get(0).getId());

        assertEquals(c2.getId(), dc.getId());
        assertEquals(c2.getFirstName(), dc.getFirstName());
        assertEquals(c2.getLastName(), dc.getLastName());
        assertEquals(c2.getEmail(), dc.getEmail());
        assertEquals(c2.getPhoneNumber(), dc.getPhoneNumber());
        assertEquals(c2.getBookings().get(0).getId(), dc.getMiniBookingDto().get(0).getId());
    }

    @Test
    void createBookingListFromMiniBookingsDtoList() {
        MiniBookingDtoForCustomer miniBookingDto = new MiniBookingDtoForCustomer
                (1L, LocalDate.now(), LocalDate.now(), new MiniRoomDto(1L, 2));
        List<MiniBookingDtoForCustomer> miniBookingDtoList = Arrays.asList(miniBookingDto);

        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(new Booking()));
        List<Booking> bookings = cs.createBookingListFromMiniBookingsDtoList(miniBookingDtoList);
        assertEquals(1, bookings.size());
    }

    @Test
    void createMiniBookingDtoListFromBookingList() {
        Booking booking = new Booking(1L, LocalDate.now(), LocalDate.now(), 1, new Room(1L, 2), new Customer());
        List<Booking> bookings = Arrays.asList(booking);

        List<MiniBookingDtoForCustomer> miniBookingDtoList = cs.createMiniBookingDtoListFromBookingList(bookings);
        assertEquals(1, miniBookingDtoList.size());
    }

    @Test
    void getAllDetailedCustomers() {
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now(), 1, new Room(1L, 2), null);
        Booking booking2 = new Booking(2L, LocalDate.now(), LocalDate.now(), 1, new Room(2L, 2), null);
        List<Booking> bookings1= List.of(booking1);
        List<Booking> bookings2 = List.of(booking1);

        Customer customer1 = new Customer(1L, "John", "Doe", "john@example.com", "123456789", bookings1);
        Customer customer2 = new Customer(2L, "Jane", "Doe", "jane@example.com", "987654321", bookings2);
        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(mockCustomerRepo.findAll()).thenReturn(customers);

        List<DetailedCustomerDto> detailedCustomers = cs.getAllDetailedCustomers();

        assertEquals(2, detailedCustomers.size());
        assertEquals(customer1.getId(), detailedCustomers.get(0).getId());
        assertEquals(customer1.getFirstName(), detailedCustomers.get(0).getFirstName());
        assertEquals(customer1.getLastName(), detailedCustomers.get(0).getLastName());
        assertEquals(customer1.getEmail(), detailedCustomers.get(0).getEmail());
        assertEquals(customer1.getPhoneNumber(), detailedCustomers.get(0).getPhoneNumber());

        assertEquals(customer2.getId(), detailedCustomers.get(1).getId());
        assertEquals(customer2.getFirstName(), detailedCustomers.get(1).getFirstName());
        assertEquals(customer2.getLastName(), detailedCustomers.get(1).getLastName());
        assertEquals(customer2.getEmail(), detailedCustomers.get(1).getEmail());
        assertEquals(customer2.getPhoneNumber(), detailedCustomers.get(1).getPhoneNumber());
    }

    @Test
    void getAllMiniCustomers() {
        Customer customer1 = new Customer(1L, "John", "Doe", "john@example.com", "123456789");
        Customer customer2 = new Customer(2L, "Jane", "Doe", "jane@example.com", "987654321");
        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(mockCustomerRepo.findAll()).thenReturn(customers);
        List<MiniCustomerDto> miniCustomers = cs.getAllMiniCustomers();

        assertEquals(2, miniCustomers.size());
        assertEquals(customer1.getId(), miniCustomers.get(0).getId());
        assertEquals(customer1.getFirstName(), miniCustomers.get(0).getFirstName());
        assertEquals(customer1.getLastName(), miniCustomers.get(0).getLastName());
        assertEquals(customer1.getEmail(), miniCustomers.get(0).getEmail());
        assertEquals(customer1.getPhoneNumber(), miniCustomers.get(0).getPhoneNumber());

        assertEquals(customer2.getId(), miniCustomers.get(1).getId());
        assertEquals(customer2.getFirstName(), miniCustomers.get(1).getFirstName());
        assertEquals(customer2.getLastName(), miniCustomers.get(1).getLastName());
        assertEquals(customer2.getEmail(), miniCustomers.get(1).getEmail());
        assertEquals(customer2.getPhoneNumber(), miniCustomers.get(1).getPhoneNumber());
    }

    @Test
    void getDetailedCustomerById() {
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now(), 1, new Room(1L, 2), null);
        List<Booking> bookings1= List.of(booking1);

        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe", "john@example.com", "123456789", bookings1);

        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        DetailedCustomerDto detailedCustomerDto = cs.getDetailedCustomerById(customerId);

        assertNotNull(detailedCustomerDto);
        assertEquals(customer.getId(), detailedCustomerDto.getId());
        assertEquals(customer.getFirstName(), detailedCustomerDto.getFirstName());
        assertEquals(customer.getLastName(), detailedCustomerDto.getLastName());
        assertEquals(customer.getEmail(), detailedCustomerDto.getEmail());
        assertEquals(customer.getPhoneNumber(), detailedCustomerDto.getPhoneNumber());
        //Hur kan jag testa bokningen?
        //assertEquals(customer.getBookings().size(), detailedCustomerDto.getMiniBookingDto().);
        //Ska jag ge kunden en MiniBookingDtoForCustomer och testa det med?
    }

    @Test
    void getMiniCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe", "john@example.com", "123456789");
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));

        MiniCustomerDto miniCustomerDto = cs.getMiniCustomerById(customerId);

        assertNotNull(miniCustomerDto);
        assertEquals(customer.getId(), miniCustomerDto.getId());
        assertEquals(customer.getFirstName(), miniCustomerDto.getFirstName());
        assertEquals(customer.getLastName(), miniCustomerDto.getLastName());
        assertEquals(customer.getEmail(), miniCustomerDto.getEmail());
        assertEquals(customer.getPhoneNumber(), miniCustomerDto.getPhoneNumber());
    }

    //Funkar ej
    @Test
    void findCustomers() {
        List<MiniCustomerDto> allCustomers = Arrays.asList(
                new MiniCustomerDto(1L, "John", "Doe", "john@example.com", "123456789"),
                new MiniCustomerDto(2L, "Jane", "Doe", "jane@example.com", "987654321"),
                new MiniCustomerDto(3L, "Alice", "Smith", "alice@example.com", "111222333")
        );
        String searchWord = "doe";

        //Här blir det fel.
        when(cs.getAllMiniCustomers()).thenReturn(allCustomers);
        List<MiniCustomerDto> customerMatches = cs.findCustomers(searchWord);

        assertEquals(2, customerMatches.size());
        assertTrue(customerMatches.stream().allMatch(
                c -> c.getFirstName().toLowerCase().contains(searchWord) ||
                        c.getLastName().toLowerCase().contains(searchWord) ||
                        c.getEmail().toLowerCase().contains(searchWord) ||
                        c.getPhoneNumber().toLowerCase().contains(searchWord)
        ));
    }

    @Test
    void updateCustomer() {
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "John", "Doe", "john@example.com", "123456789");
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        cs.updateCustomer(customerId, "Jane", "Smith", "jane@example.com", "987654321");

        verify(mockCustomerRepo, times(1)).save(customer);

        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals("987654321", customer.getPhoneNumber());

    }

    //ChatGPT
    //Funkar ej
    @Test
    void addCustomer_Success() {
        Customer customer = new Customer(1L, "John", "Doe", "john@example.com", "123456789");
        when(cs.validator.validate(customer)).thenReturn(new HashSet<>());

        String result = cs.addCustomer(customer);
        verify(mockCustomerRepo, times(1)).save(customer);
        assertEquals("Success!", result);
    }

    //ChatGPT
    //Funkar ej
    @Test
    void addCustomer_Failure() {
        Customer invalidCustomer = new Customer();

        Set<ConstraintViolation<Customer>> violations = new HashSet<>();
        violations.add(mock(ConstraintViolation.class));
        when(cs.validator.validate(invalidCustomer)).thenReturn(violations);

        String result = cs.addCustomer(invalidCustomer);
        verify(mockCustomerRepo, never()).save(any());

        assertNotEquals("Success!", result);
        assertTrue(result.contains("Error"));
    }

    @Test
    void deleteCustomerById() {
        Long customerId = 1L;
        Customer testCustomer = new Customer(customerId, "John", "Doe", "john@example.com", "123456789");

        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(testCustomer));
        cs.deleteCustomerById(customerId);
        verify(mockCustomerRepo, times(1)).deleteById(customerId);
    }

    @Test
    void findByEmail() {
        String email = "john@example.com";
        Customer customer = new Customer(1L, "John", "Doe", email, "123456789");

        when(mockCustomerRepo.findByEmail(email)).thenReturn(customer);
        Customer foundCustomer = cs.findByEmail(email);

        assertNotNull(foundCustomer);
        assertEquals(customer.getId(), foundCustomer.getId());
        assertEquals(customer.getFirstName(), foundCustomer.getFirstName());
        assertEquals(customer.getLastName(), foundCustomer.getLastName());
        assertEquals(customer.getEmail(), foundCustomer.getEmail());
        assertEquals(customer.getPhoneNumber(), foundCustomer.getPhoneNumber());
    }
}