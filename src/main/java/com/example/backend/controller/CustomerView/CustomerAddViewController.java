package com.example.backend.controller.CustomerView;

import com.example.backend.model.Customer;
import com.example.backend.services.impl.CustomerServicesImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/Customer")
public class CustomerAddViewController {
    private final CustomerServicesImpl cs;

    public CustomerAddViewController(CustomerServicesImpl cs){
        this.cs = cs;
    }

    @RequestMapping("/addCustomer")
    public String addCustomer(){
        return "Customer/addCustomer.html";
    }

    @PostMapping("/addCustomerSuccess")
    public String addCustomerSuccess(@RequestParam String firstName, @RequestParam String lastName,
                                     @RequestParam String pNr, @RequestParam String email, Model model){
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("pNr", pNr);
        model.addAttribute("email", email);

        Customer test = cs.findByEmail(email);

        if(test == null){
            cs.addCustomer(new Customer(firstName, lastName, email, pNr));
            model.addAttribute("header", "Customer added!");
            return "Customer/addCustomer.html";
        }else{
            model.addAttribute("header", "Error. Please try again.");
            return "Customer/addCustomer.html";
        }
    }
}
