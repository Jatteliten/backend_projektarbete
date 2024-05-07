package com.example.backend.model.modeluitl;

import com.example.backend.model.ContractCustomer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@JacksonXmlRootElement(localName = "allcustomers")
@Data
public class ContractCustomers {
    @JacksonXmlProperty(localName = "customers")
    private List<ContractCustomer> contractCustomers;
}
