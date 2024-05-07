package com.example.backend.repos;

import com.example.backend.model.ContractCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractCustomerRepo extends JpaRepository<ContractCustomer, Long> {
    ContractCustomer findByExternalSystemId(String id);
}
