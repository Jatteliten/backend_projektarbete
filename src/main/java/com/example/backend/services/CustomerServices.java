package com.example.backend.services;

import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Customer;

import java.util.List;

public interface CustomerServices {
    DetailedCustomerDto customerToDetailedCustomerDto(Customer c);
    MiniCustomerDto customerToMiniCustomerDto(Customer c);
    Customer miniCustomerDtoToCustomer(MiniCustomerDto c);
    Customer detailedCustomerDtoToCustomer(DetailedCustomerDto c);
    List<DetailedCustomerDto> getAllDetailedCustomers();
    List<MiniCustomerDto> getAllMiniCustomers();
    DetailedCustomerDto getDetailedCustomerById(Long id);
    String addCustomer(Customer c);
    void deleteCustomerById(Long id);
    String updateCustomer(MiniCustomerDto c);
    List<MiniCustomerDto> findCustomers(String searchWord);
    MiniCustomerDto getMiniCustomerById(Long id);
    Customer findByEmail(String email);
    String addCustomerWithFindById(MiniCustomerDto c);

}
