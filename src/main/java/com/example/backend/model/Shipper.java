package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Shipper {
    @Id
    @GeneratedValue
    private Long id;

    private int externalId;
    private String email;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;



    public Shipper(int externalId, String email, String companyName, String contactName, String contactTitle, String streetAddress, String city, String postalCode, String country, String phone, String fax) {
        this.externalId = externalId;
        this.email = email;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
    }
}
