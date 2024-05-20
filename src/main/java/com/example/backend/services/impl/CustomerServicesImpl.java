package com.example.backend.services.impl;

import com.example.backend.Dto.BookingViews.MiniBookingDtoForCustomer;
import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.Dto.RoomViews.MiniRoomDto;
import com.example.backend.model.Booking;
import com.example.backend.model.Customer;
import com.example.backend.repos.BookingRepo;
import com.example.backend.repos.CustomerRepo;
import com.example.backend.services.CustomerServices;
import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServicesImpl implements CustomerServices {
    private final CustomerRepo cr;
    private final BookingRepo br;
    public final Validator validator;

    public CustomerServicesImpl(CustomerRepo cr, BookingRepo br) {
        this.cr = cr;
        this.br = br;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public DetailedCustomerDto customerToDetailedCustomerDto(Customer c) {
        return DetailedCustomerDto.builder()
                .id(c.getId()).phoneNumber(c.getPhoneNumber())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .miniBookingDto(createMiniBookingDtoListFromBookingList(c.getBookings()))
                .build();
    }

    public MiniCustomerDto customerToMiniCustomerDto(Customer c) {
        return MiniCustomerDto.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .build();
    }

    public Customer miniCustomerDtoToCustomer(MiniCustomerDto c) {
        return Customer.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .build();
    }

    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c) {
        return Customer.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .phoneNumber(c.getPhoneNumber())
                .bookings(createBookingListFromMiniBookingsDtoList(c.getMiniBookingDto()))
                .build();
    }

    public List<Booking> createBookingListFromMiniBookingsDtoList(List<MiniBookingDtoForCustomer> b) {
        return b.stream().map(bb -> br.findById(bb.getId()).get()).toList();
    }

    public List<MiniBookingDtoForCustomer> createMiniBookingDtoListFromBookingList(List<Booking> b) {
        return b.stream().map(bb -> MiniBookingDtoForCustomer.builder()
                .id(bb.getId())
                .startDate(bb.getStartDate())
                .endDate(bb.getEndDate())
                .miniRoomDto(MiniRoomDto.builder()
                        .id(bb.getRoom().getId())
                        .size(bb.getRoom().getSize())
                        .pricePerNight(bb.getRoom().getPricePerNight())
                        .build())
                .build())
                .collect(Collectors.toList());
    }

    public List<DetailedCustomerDto> getAllDetailedCustomers() {
        return cr.findAll().stream().map(c -> customerToDetailedCustomerDto(c)).toList();
    }

    public List<MiniCustomerDto> getAllMiniCustomers() {
        return cr.findAll().stream().map(c -> customerToMiniCustomerDto(c)).toList();
    }

    public DetailedCustomerDto getDetailedCustomerById(Long id) {
        return customerToDetailedCustomerDto(cr.findById(id).get());
    }

    @Override
    public MiniCustomerDto getMiniCustomerById(Long id) {
        Customer customer = cr.findById(id).get();
        return customerToMiniCustomerDto(customer);
    }

    @Override
    public List<MiniCustomerDto> findCustomers(String searchWord) {
        List<MiniCustomerDto> customerMatches = new ArrayList<>();
        searchWord = searchWord.toLowerCase();
        List<MiniCustomerDto> allCustomers = getAllMiniCustomers();
        for (MiniCustomerDto c : allCustomers) {
            if (
                    c.getId().toString().toLowerCase().contains(searchWord) ||
                    c.getEmail().toLowerCase().contains(searchWord) ||
                    c.getFirstName().toLowerCase().contains(searchWord) ||
                    c.getLastName().toLowerCase().contains(searchWord) ||
                    c.getPhoneNumber().toLowerCase().contains(searchWord)
            ){
                    customerMatches.add(c);
            }
        }
        return customerMatches;
    }

    @Override
    public String updateCustomer(MiniCustomerDto c) {

        Customer customer = cr.findById(c.getId()).get();
        customer.setFirstName(c.getFirstName().trim());
        customer.setLastName(c.getLastName().trim());
        customer.setEmail(c.getEmail().trim());
        customer.setPhoneNumber(c.getPhoneNumber().trim());

        return addCustomer(customer);
    }

    @Override
    public String addCustomer(Customer c) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(c);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Customer> violation : violations) {
                errorMessages.append(" - ").append(violation.getMessage());
            }
            return String.valueOf(errorMessages);
        }
        else {
            cr.save(c);
            return "Success!";
        }
    }

    public String addCustomerWithFindById(MiniCustomerDto c) {
        Customer customer = cr.findById(c.getId()).orElse(null);

        if (customer != null) {
            String originalFirstName = customer.getFirstName();
            String originalLastName = customer.getLastName();
            String originalEmail = customer.getEmail();
            String originalPhoneNumber = customer.getPhoneNumber();

            try {
                customer.setFirstName(c.getFirstName().trim());
                customer.setLastName(c.getLastName().trim());
                customer.setEmail(c.getEmail().trim());
                customer.setPhoneNumber(c.getPhoneNumber().trim());

                Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

                if (violations.isEmpty()) {
                    cr.save(customer);
                    return "Success!";
                } else {
                    customer.setFirstName(originalFirstName);
                    customer.setLastName(originalLastName);
                    customer.setEmail(originalEmail);
                    customer.setPhoneNumber(originalPhoneNumber);
                    StringBuilder errorMessages = new StringBuilder();
                    for (ConstraintViolation<Customer> violation : violations) {
                        errorMessages.append(" - ").append(violation.getMessage());
                    }
                    return String.valueOf(errorMessages);
                }
            } catch (Exception e) {
                customer.setFirstName(originalFirstName);
                customer.setLastName(originalLastName);
                customer.setEmail(originalEmail);
                customer.setPhoneNumber(originalPhoneNumber);
                return "An error occurred while updating the customer.";
            }
        } else {
            return "Customer not found.";
        }
    }


    @Override
    public void deleteCustomerById(Long id) {
        cr.deleteById(id);
    }

    public Customer findByEmail(String email){
        return cr.findByEmail(email);
    }

}
