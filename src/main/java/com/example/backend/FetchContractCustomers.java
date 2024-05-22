package com.example.backend;

import com.example.backend.model.ContractCustomer;
import com.example.backend.model.Customer;
import com.example.backend.model.modelUti.ContractCustomers;
import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.XmlStreamProvider;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Set;

@ComponentScan
public class FetchContractCustomers implements CommandLineRunner {
    private final ContractCustomerRepo ccr;
    XmlStreamProvider xmlStreamProvider;
    public final Validator validator;

    public FetchContractCustomers(ContractCustomerRepo ccr, XmlStreamProvider xmlStreamProvider){
        this.ccr = ccr;
        this.xmlStreamProvider = xmlStreamProvider;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public void run(String... args) throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        InputStream stream = xmlStreamProvider.getContractCustomersStream();
        ContractCustomers customers = xmlMapper.readValue(stream, ContractCustomers.class);

        for (ContractCustomer c : customers.getContractCustomers()) {
            validateContractCustomer(c, false);

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


    public String validateContractCustomer(ContractCustomer concust, Boolean isTest) {
        Set<ConstraintViolation<ContractCustomer>> violations = validator.validate(concust);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            errorMessages.append("XML error for contract customer: ");
            for (ConstraintViolation<ContractCustomer> violation : violations) {
                errorMessages.append(" - ").append(violation.getMessage());
            }
            if (!isTest) {
                throw new InputMismatchException(errorMessages.toString());
            }
            else {
                return errorMessages.toString();
            }
        }
        return null;
    }

}
