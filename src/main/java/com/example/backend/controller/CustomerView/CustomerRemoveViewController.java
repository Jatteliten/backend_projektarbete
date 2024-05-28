package com.example.backend.controller.CustomerView;


import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.services.CustomerServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Customer")
public class CustomerRemoveViewController {

    private final CustomerServices customerServices;

    public CustomerRemoveViewController(CustomerServices customerServices) {
        this.customerServices = customerServices;
    }

    @RequestMapping("/allWithDelete")
    @PreAuthorize("isAuthenticated()")
    public String allWithDelete(Model model) {
        List<MiniCustomerDto> customers = customerServices.getAllMiniCustomers();
        model.addAttribute("allCustomers", customers);
        model.addAttribute("header", "All Customers");
        return "Customer/removeCustomer.html";
    }

    @RequestMapping("/deleteById/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteById(@PathVariable Long id, Model model) {
        customerServices.deleteCustomerById(id);
        return allWithDelete(model);
    }

    @RequestMapping("/filter/delete")
    @PreAuthorize("isAuthenticated()")
    public String filter(@RequestParam String input, Model model) {
        List<MiniCustomerDto> customers = customerServices.findCustomers(input);

        if (!customers.isEmpty()) {
            model.addAttribute("allCustomers", customers);
            model.addAttribute("header", "Matches Found");
        } else {
            model.addAttribute("header", "No Matches Found");
        }
        return "Customer/removeCustomer.html";
    }

}
