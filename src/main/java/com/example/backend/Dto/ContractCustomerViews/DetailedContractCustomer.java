package com.example.backend.Dto.ContractCustomerViews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedContractCustomer {
    private String externalSystemId;

    private String companyName;

    private String contactName;

    private String contactTitle;

    private String streetAddress;

    private String city;

    private String postalCode;

    private String country;

    private String phone;

    private String fax;
}
