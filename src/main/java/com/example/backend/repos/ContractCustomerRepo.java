package com.example.backend.repos;

import com.example.backend.model.ContractCustomer;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractCustomerRepo extends CrudRepository<ContractCustomer, Long> {
    ContractCustomer findByExternalSystemId(String id);

    List<ContractCustomer> findAll(Sort sort);

    List<ContractCustomer> findAllByCompanyNameContains(String companyName, Sort sort);

    List<ContractCustomer> findAllByCompanyNameContainsOrContactNameContains(String companyName, String ContactName, Sort sort);

}
