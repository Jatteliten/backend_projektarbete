package com.example.backend;

import com.example.backend.model.ContractCustomer;
import com.example.backend.model.modelUti.ContractCustomers;
import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.XmlStreamProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.io.InputStream;
import java.util.Optional;

@ComponentScan
public class FetchContractCustomers implements CommandLineRunner {
    private final ContractCustomerRepo ccr;
    XmlStreamProvider xmlStreamProvider;

    public FetchContractCustomers(ContractCustomerRepo ccr, XmlStreamProvider xmlStreamProvider){
        this.ccr = ccr;
        this.xmlStreamProvider = xmlStreamProvider;
    }

    @Override
    public void run(String... args) throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        InputStream stream = xmlStreamProvider.getContractCustomersStream();
        ContractCustomers customers = xmlMapper.readValue(stream, ContractCustomers.class);

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
