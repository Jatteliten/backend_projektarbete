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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;


class CustomerServicesImplTests {

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
        MockitoAnnotations.openMocks(this);
        c = Customer.builder().id(1L).firstName("Daniel").lastName("Isaksson").email("Daniel@hej.com")
                .phoneNumber("0722055577").build();
        b = List.of(new Booking(1L, LocalDate.of(2022, 10, 10),
                LocalDate.of(2022, 10, 12), 1, Room.builder().id(1L).size(3).build(), c));
        c.setBookings(b);

        dc = cs.customerToDetailedCustomerDto(c);
        mc = cs.customerToMiniCustomerDto(c);

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
                (1L, LocalDate.now(), LocalDate.now(), MiniRoomDto.builder().id(1L).size(2).build());
        List<MiniBookingDtoForCustomer> miniBookingDtoList = List.of(miniBookingDto);

        when(mockBookingRepo.findById(1L)).thenReturn(Optional.of(new Booking()));
        List<Booking> bookings = cs.createBookingListFromMiniBookingsDtoList(miniBookingDtoList);
        assertEquals(1, bookings.size());
    }

    @Test
    void createMiniBookingDtoListFromBookingList() {
        Booking booking = new Booking(1L, LocalDate.now(), LocalDate.now(), 1,
                Room.builder().id(1L).size(2).build(), new Customer());
        List<Booking> bookings = List.of(booking);

        List<MiniBookingDtoForCustomer> miniBookingDtoList = cs.createMiniBookingDtoListFromBookingList(bookings);
        assertEquals(1, miniBookingDtoList.size());
    }

    @Test
    void getAllDetailedCustomers() {
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now(), 1,
                Room.builder().id(1L).size(2).build(), null);
        Booking booking2 = new Booking(2L, LocalDate.now(), LocalDate.now(), 1,
                Room.builder().id(2L).size(2).build(), null);
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
        Customer customer1 = Customer.builder().id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phoneNumber("123456789").build();
        Customer customer2 = Customer.builder().id(2L).firstName("Jane").lastName("Doe")
                .email("jane@example.com").phoneNumber("987654321").build();
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
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now(), 1,
                Room.builder().id(1L).size(2).build(), null);
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
    }

    @Test
    void getMiniCustomerById() {
        Long customerId = 1L;
        Customer customer = Customer.builder().id(customerId).firstName("John").lastName("Doe")
                .email("john@example.com").phoneNumber("123456789").build();
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));

        MiniCustomerDto miniCustomerDto = cs.getMiniCustomerById(customerId);

        assertNotNull(miniCustomerDto);
        assertEquals(customer.getId(), miniCustomerDto.getId());
        assertEquals(customer.getFirstName(), miniCustomerDto.getFirstName());
        assertEquals(customer.getLastName(), miniCustomerDto.getLastName());
        assertEquals(customer.getEmail(), miniCustomerDto.getEmail());
        assertEquals(customer.getPhoneNumber(), miniCustomerDto.getPhoneNumber());
    }


    @Test
    void findCustomers() {
        List<Customer> allCustomers = Arrays.asList(Customer.builder().id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phoneNumber("123456789").build(),
                Customer.builder().id(2L).firstName("Jane").lastName("Doe")
                        .email("jane@example.com").phoneNumber("987654321").build(),
                Customer.builder().id(3L).firstName("Alice").lastName("Smith")
                        .email("alice@example.com").phoneNumber("111222333").build()
        );
        String searchWord = "doe";

        when(mockCustomerRepo.findAll()).thenReturn(allCustomers);
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
    void addCustomer_Success() {
        Customer customer = Customer.builder().id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").phoneNumber("123456789").build();
        String result = cs.addCustomer(customer);
        verify(mockCustomerRepo, times(1)).save(customer);
        assertEquals("Success!", result);
    }

    @Test
    void addCustomer_Failure() {
        Customer invalidCustomer = new Customer();
        String result = cs.addCustomer(invalidCustomer);
        verify(mockCustomerRepo, never()).save(any());
        assertNotEquals("Success!", result);
        assertTrue(result.contains("required"));
    }

    @Test
    void deleteCustomerById() {
        Long customerId = 1L;
        Customer testCustomer = Customer.builder().id(customerId).firstName("John").lastName("Doe")
                .email("john@example.com").phoneNumber("123456789").build();

        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(testCustomer));
        cs.deleteCustomerById(customerId);
        verify(mockCustomerRepo, times(1)).deleteById(customerId);
    }

    @Test
    void findByEmail() {
        String email = "john@example.com";
        Customer customer = Customer.builder().id(1L).firstName("John").lastName("Doe")
                .email(email).phoneNumber("123456789").build();

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