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

        /*
        String inputFeedback = validateInput(firstName, lastName, pNr, email);
        if (!inputFeedback.isEmpty()) {
            model.addAttribute("header", "Error:" + inputFeedback);
            return addCustomer();
        }
        */

        Customer test = cs.findByEmail(email);

        if(test == null){
            //cs.addCustomer(new Customer(firstName, lastName, email, pNr));
            model.addAttribute("header", cs.addCustomer(new Customer(firstName, lastName, email, pNr)));
            return addCustomer();
        }else{
            model.addAttribute("header", "Customer already exists.");
            return addCustomer();
        }
    }


    private String validateInput(String firstName, String lastName, String pNr, String email) {
        String errorMessage = "";
        if (firstName.isEmpty() || lastName.isEmpty() || pNr.isEmpty() || email.isEmpty()) {
            errorMessage = errorMessage + " One or several fields are blank.";
            return errorMessage;
        }
        if (!email.contains("@")) {
            errorMessage = errorMessage + " Email address is filled out incorrectly.";
        }
        if (pNr.length() < 10 || pNr.length() > 15) {
            errorMessage = errorMessage + " Phone number is too short or too long.";
        }
        return errorMessage;
    }
}
