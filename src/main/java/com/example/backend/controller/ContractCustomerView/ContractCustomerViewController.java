package com.example.backend.controller.ContractCustomerView;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ContractCustomer")
public class ContractCustomerViewController {

    @RequestMapping("/viewCustomer")
    public String addCustomer(){
        return "ContractCustomer/showContractCustomer.html";
    }


}
