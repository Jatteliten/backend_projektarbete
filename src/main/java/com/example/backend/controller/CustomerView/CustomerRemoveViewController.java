package com.example.backend.controller.CustomerView;


import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Customer;
import com.example.backend.services.impl.CustomerServicesImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Customer")
public class CustomerRemoveViewController {

    private final CustomerServicesImpl customerServices;

    public CustomerRemoveViewController(CustomerServicesImpl customerServices) {
        this.customerServices = customerServices;
    }

    @RequestMapping("/allWithDelete")
    public String allWithDelete(Model model) {
        List<MiniCustomerDto> customers = customerServices.getAllMiniCustomers();
        model.addAttribute("allCustomers", customers);
        model.addAttribute("header", "All Customers");
        return "Customer/removeCustomer.html";
    }

    @RequestMapping("/deleteById/{id}")
    public String deleteById(@PathVariable Long id, Model model) {
        customerServices.deleteCustomerById(id);
        return allWithDelete(model);
    }

    @PostMapping("/filter/delete")
    public String filter(@RequestParam String input, Model model) {
        List<MiniCustomerDto> customers = customerServices.findCustomers(input);

        if (!customers.isEmpty()) {
            model.addAttribute("allCustomers", customers);
            model.addAttribute("header", "Matches Found");
            return "Customer/removeCustomer.html";
        } else {
            model.addAttribute("header", "No Matches Found");
            return "Customer/removeCustomer.html";
        }
    }

}
