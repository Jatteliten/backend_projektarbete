package com.example.backend.controller.ContractCustomerView;

import com.example.backend.Dto.ContractCustomerViews.DetailedContractCustomer;
import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.services.ContractCustomerServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/ContractCustomer")
public class ContractCustomerViewController {

    private final ContractCustomerServices contractCustomerServices;

    public ContractCustomerViewController(ContractCustomerServices contractCustomerServices) {
        this.contractCustomerServices = contractCustomerServices;
    }


    @RequestMapping("/viewCustomer")
    public String viewCustomer(Model model){
        List<MiniContractCustomerDto> concust = contractCustomerServices.getAllMiniContractCustomerDto();

        model.addAttribute("allContractCustomers", concust);
        return "ContractCustomer/showContractCustomer.html";
    }

    @RequestMapping("/{externalId}")
    public String detailedContractCustomer(@PathVariable String externalId, Model model){
        List<DetailedContractCustomer> detailedContractCustomer = List.of(
                contractCustomerServices.contractCustomerToDetailedContractCustomerDto(
                contractCustomerServices.getSpecifikContractCustomer(externalId))
        );
        model.addAttribute("allContractCustomers", detailedContractCustomer);
        return "ContractCustomer/showFullContractCustomer.html";
    }



}