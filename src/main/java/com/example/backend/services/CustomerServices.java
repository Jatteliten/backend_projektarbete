package com.example.backend.services;

import com.example.backend.repos.CustomerRepo;
import org.springframework.stereotype.Service;

@Service
public class CustomerServices {
    private final CustomerRepo cr;

    public CustomerServices(CustomerRepo cr){
        this.cr = cr;
    }
}
