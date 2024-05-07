package com.example.backend.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "customers")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractCustomer {
    @Id
    @GeneratedValue
    private Long customerId;

    @JacksonXmlProperty(localName = "id")
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
