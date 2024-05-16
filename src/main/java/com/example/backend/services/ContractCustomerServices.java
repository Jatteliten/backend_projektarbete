package com.example.backend.services;

import com.example.backend.Dto.ContractCustomerViews.DetailedContractCustomer;
import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.model.ContractCustomer;

public interface ContractCustomerServices {
    MiniContractCustomerDto contractCustomerToMiniContractCustomerDto(ContractCustomer c);

    ContractCustomer getSpecificContractCustomer(String externalId);

    DetailedContractCustomer contractCustomerToDetailedContractCustomerDto(ContractCustomer contractCustomer);
}
