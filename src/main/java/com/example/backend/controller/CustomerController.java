package com.example.backend.controller;

import com.example.backend.Dto.CustomerViews.DetailedCustomerDto;
import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.services.CustomerServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerServices cs;

    public CustomerController(CustomerServices cs){
        this.cs = cs;
    }

    @RequestMapping("/getAll")
    List<DetailedCustomerDto> getAllCustomers(){
        return cs.getAllDetailedCustomers();
    }

    @RequestMapping("/getAllMini")
    List<MiniCustomerDto> getAllMiniCustomers() { return cs.getAllMiniCustomers();}

    @RequestMapping("/getBooking/{id}")
    DetailedCustomerDto getCustomerById(@PathVariable Long id){
        return cs.getDetailedCustomerById(id);
    }




}
