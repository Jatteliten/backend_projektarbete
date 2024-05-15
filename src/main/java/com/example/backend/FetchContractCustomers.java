package com.example.backend;

import com.example.backend.model.ContractCustomer;
import com.example.backend.model.modelUti.ContractCustomers;
import com.example.backend.repos.ContractCustomerRepo;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.net.URI;
import java.util.Optional;

@ComponentScan
public class FetchContractCustomers implements CommandLineRunner {
    private final ContractCustomerRepo ccr;

    public FetchContractCustomers(ContractCustomerRepo ccr){
        this.ccr = ccr;
    }

    @Override
    public void run(String... args) throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        ContractCustomers customers = xmlMapper.readValue(
                new URI("https://javaintegration.systementor.se/customers").toURL(),
                ContractCustomers.class);

        for (ContractCustomer c : customers.getContractCustomers()) {
            Optional<ContractCustomer> tempCustomer = Optional.ofNullable(ccr.findByExternalSystemId(c.getExternalSystemId()));
            if (tempCustomer.isPresent()) {
                ContractCustomer existingCustomer = findAndApplyChangesToContractCustomer(c, tempCustomer);
                ccr.save(existingCustomer);
            } else {
                ccr.save(c);
            }
        }
    }

    public ContractCustomer findAndApplyChangesToContractCustomer(ContractCustomer c, Optional<ContractCustomer> tempCustomer) {
        ContractCustomer existingCustomer = tempCustomer.get();
        existingCustomer.setCompanyName(c.getCompanyName());
        existingCustomer.setContactName(c.getContactName());
        existingCustomer.setContactTitle(c.getContactTitle());
        existingCustomer.setStreetAddress(c.getStreetAddress());
        existingCustomer.setCity(c.getCity());
        existingCustomer.setPostalCode(c.getPostalCode());
        existingCustomer.setCountry(c.getCountry());
        existingCustomer.setPhone(c.getPhone());
        existingCustomer.setFax(c.getFax());
        return existingCustomer;
    }

}
