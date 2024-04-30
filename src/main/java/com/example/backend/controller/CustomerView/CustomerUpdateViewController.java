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

    //När man väljer customer som ska uppdateras sätts fälten i formuläret till
    // den valda customerns värden.
    @RequestMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        MiniCustomerDto miniCustomerDto = customerServices.getMiniCustomerById(id);
        model.addAttribute("customer", miniCustomerDto);

        return "Customer/updateCustomerForm.html";
    }

    //Ska kallas på när man fyllt i formulär
    @PostMapping("/update/final")
    public String updateByAll(MiniCustomerDto customer, Model model) {
        model.addAttribute("message", customerServices.addCustomer2(customer));
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
