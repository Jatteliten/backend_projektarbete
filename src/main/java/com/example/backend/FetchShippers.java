package com.example.backend;

import com.example.backend.model.Shipper;
import com.example.backend.model.modelUti.ShipperDto;
import com.example.backend.repos.ShipperRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.net.URI;

@ComponentScan
public class FetchShippers implements CommandLineRunner {
    private final ShipperRepo sr;

    public FetchShippers(ShipperRepo sr){
        this.sr = sr;
    }
    @Override
    public void run(String... args) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ShipperDto[] shippers = objectMapper.readValue(
                new URI("https://javaintegration.systementor.se/shippers").toURL(),
                ShipperDto[].class);

        for(ShipperDto s : shippers){
            Shipper shipper = sr.findByExternalId(s.id);
            if (shipper != null){
                updateShipper(shipper,s);
                sr.save(shipper);
            } else {
                sr.save(createNewShipper(s));
            }
        }

    }

    private void updateShipper(Shipper shipper, ShipperDto s){
        shipper.setEmail(s.getEmail());
        shipper.setCompanyName(s.getCompanyName());
        shipper.setContactName(s.getContactName());
        shipper.setContactTitle(s.getContactTitle());
        shipper.setStreetAddress(s.getStreetAddress());
        shipper.setCity(s.getCity());
        shipper.setCountry(s.getCountry());
        shipper.setPostalCode(s.getPostalCode());
        shipper.setPhone(s.getPhone());
        shipper.setFax(s.getFax());
    }

    private Shipper createNewShipper(ShipperDto s){
        return Shipper.builder()
                .externalId(s.id)
                .email(s.email)
                .companyName(s.companyName)
                .contactName(s.contactName)
                .contactTitle(s.contactTitle)
                .streetAddress(s.streetAddress)
                .city(s.city)
                .postalCode(s.postalCode)
                .country(s.country)
                .phone(s.phone)
                .fax(s.fax).build();
    }



}

