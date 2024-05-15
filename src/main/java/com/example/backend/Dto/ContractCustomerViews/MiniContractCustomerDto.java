package com.example.backend.Dto.ContractCustomerViews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniContractCustomerDto {

    private long id;
    private String companyName;

    private String contactName;

    private String country;

    private String externalSystemId;
}