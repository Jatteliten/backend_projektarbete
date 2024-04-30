package com.example.backend.services;

import com.example.backend.Dto.BookingViews.DetailedBookingDto;
import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Customer;

import java.util.List;

public interface CustomerServices {
    public DetailedCustomerDto customerToDetailedCustomerDto(Customer c);
    public MiniCustomerDto customerToMiniCustomerDto(Customer c);
    public Customer miniCustomerDtoToCustomer(MiniCustomerDto c);
    public Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c);
    public List<DetailedCustomerDto> getAllDetailedCustomers();
    public List<MiniCustomerDto> getAllMiniCustomers();
    public DetailedCustomerDto getDetailedCustomerById(Long id);

    public String addCustomer(Customer c);
    public void deleteCustomer(Customer c);
    public void deleteCustomerById(Long id);
    public String updateCustomer(MiniCustomerDto c);
    public List<MiniCustomerDto> findCustomers(String searchWord);
    public MiniCustomerDto getMiniCustomerById(Long id);
}
