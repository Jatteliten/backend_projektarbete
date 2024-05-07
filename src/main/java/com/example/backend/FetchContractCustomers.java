package com.example.backend;

import com.example.backend.model.ContractCustomer;
import com.example.backend.model.modeluitl.ContractCustomers;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import java.net.URI;

@ComponentScan
public class FetchContractCustomers implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception{
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        ContractCustomers customers = xmlMapper.readValue(
                new URI("https://javaintegration.systementor.se/customers").toURL(),
                ContractCustomers.class);

        for(ContractCustomer c: customers.getContractCustomers()){
            System.out.println(c.getContactName());
        }
    }
}
