package com.example.backend.services.impl;

import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.ContractCustomerServices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractCustomerServicesImpl implements ContractCustomerServices {
    private final ContractCustomerRepo ccr;

    public ContractCustomerServicesImpl(ContractCustomerRepo ccr) {
        this.ccr = ccr;
    }
    @Override
    public MiniContractCustomerDto contractCustomerToMiniContractCustomerDto(ContractCustomer c) {
        return MiniContractCustomerDto.builder().id(c.getCustomerId()).companyName(c.getCompanyName())
                .contactName(c.getContactName()).country(c.getCountry()).build();

    }

    @Override
    public List<MiniContractCustomerDto> getAllMiniContractCustomerDto() {
        return ccr.findAll().stream().map(c -> contractCustomerToMiniContractCustomerDto(c)).toList();
    }

}
