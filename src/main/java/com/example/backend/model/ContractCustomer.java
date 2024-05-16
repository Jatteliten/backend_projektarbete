package com.example.backend.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotEmpty (message = "Company name is empty")
    private String companyName;

    @NotEmpty (message = "Contact name is empty")
    private String contactName;

    @NotEmpty (message = "Contact title is empty")
    private String contactTitle;

    @NotEmpty (message = "Street address is empty")
    private String streetAddress;

    @NotEmpty (message = "City name is empty")
    private String city;

    @NotEmpty (message = "Postal code is empty")
    @Pattern(regexp="^[0-9]*$", message="Wrong postal code format")
    private String postalCode;

    @NotEmpty (message = "Country is empty")
    private String country;

    @NotEmpty (message = "Phone nr is empty")
    @Pattern(regexp="^[0-9+()-]*$", message="Wrong phone nr format")
    @Size(min = 7, max = 15, message="Wrong phone nr format")
    private String phone;

    @NotEmpty (message = "Fax nr is empty")
    @Pattern(regexp="^[0-9+()-]*$", message="Wrong fax nr format")
    @Size(min = 7, max = 15, message="Wrong fax nr format")
    private String fax;
}
