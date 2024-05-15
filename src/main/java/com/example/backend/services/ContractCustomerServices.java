package com.example.backend.services;

import com.example.backend.Dto.ContractCustomerViews.DetailedContractCustomer;
import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.ContractCustomer;
import com.example.backend.model.Customer;

import java.util.List;

public interface ContractCustomerServices {
    MiniContractCustomerDto contractCustomerToMiniContractCustomerDto(ContractCustomer c);

    ContractCustomer getSpecifikContractCustomer(String externalId);

    DetailedContractCustomer contractCustomerToDetailedContractCustomerDto(ContractCustomer contractCustomer);
}
