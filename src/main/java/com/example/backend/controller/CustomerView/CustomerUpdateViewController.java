package com.example.backend.controller.CustomerView;

import com.example.backend.Dto.CustomerViews.MiniCustomerDto;
import com.example.backend.model.Customer;
import com.example.backend.services.impl.CustomerServicesImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Customer")
public class CustomerUpdateViewController {

    private final CustomerServicesImpl customerServices;

    public CustomerUpdateViewController(CustomerServicesImpl customerServices) {
        this.customerServices = customerServices;
    }

    @RequestMapping("/allWithUpdate")
    public String allWithUpdate(Model model) {
        List<MiniCustomerDto> customers = customerServices.getAllMiniCustomers();
        model.addAttribute("allCustomers", customers);
        model.addAttribute("header", "All Customers");
        return "Customer/updateCustomer.html";
    }

    @RequestMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        MiniCustomerDto miniCustomerDto = customerServices.getMiniCustomerById(id);
        model.addAttribute("customer", miniCustomerDto);
        /*
        model.addAttribute("id", id);
        model.addAttribute("fName", miniCustomerDto.getFirstName());
        model.addAttribute("lName", miniCustomerDto.getLastName());
        model.addAttribute("email", miniCustomerDto.getEmail());
        model.addAttribute("phoneNumber", miniCustomerDto.getPhoneNumber());

         */
        return "Customer/updateCustomerForm.html";
    }

    //Ska kallas på när man fyllt i formulär
    @PostMapping("/update/final")
    public String updateByAll(@Valid Customer customer, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "Customer/updateCustomerForm.html";
        }

        customerServices.updateCustomer(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber());

        model.addAttribute("message", "Customer updated successfully!");
        return allWithUpdate(model);
    }

    @PostMapping("/filter/update")
    public String filter(@RequestParam String input, Model model) {
        List<MiniCustomerDto> customers = customerServices.findCustomers(input);

        if (!customers.isEmpty()) {
            model.addAttribute("allCustomers", customers);
            model.addAttribute("header", "Matches Found");
        } else {
            model.addAttribute("header", "No Matches Found");
        }
        return "Customer/updateCustomer.html";
    }
}
