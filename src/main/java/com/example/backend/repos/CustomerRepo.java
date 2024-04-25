package com.example.backend.repos;

import com.example.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    public Customer findByEmail(String email);
}
