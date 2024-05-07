package com.example.backend.controller.CustomerView;

import com.example.backend.model.Customer;
import com.example.backend.services.CustomerServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/Customer")
public class CustomerAddViewController {
    private final CustomerServices cs;

    public CustomerAddViewController(CustomerServices cs){
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
            model.addAttribute("header", cs.addCustomer(new Customer(firstName, lastName, email, pNr)));
            return addCustomer();
        }else{
            model.addAttribute("header", "Customer already exists.");
            return addCustomer();
        }
    }

}
