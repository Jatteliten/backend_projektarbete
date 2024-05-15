package com.example.backend.controller.ContractCustomerView;

import com.example.backend.Dto.ContractCustomerViews.DetailedContractCustomer;
import com.example.backend.Dto.ContractCustomerViews.MiniContractCustomerDto;
import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import com.example.backend.services.ContractCustomerServices;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/ContractCustomer")
public class ContractCustomerViewController {

    private final ContractCustomerServices contractCustomerServices;
    private ContractCustomerRepo repo;

    public ContractCustomerViewController(ContractCustomerServices contractCustomerServices, ContractCustomerRepo repo) {
        this.contractCustomerServices = contractCustomerServices;
        this.repo = repo;
    }


    @GetMapping("/viewCustomer")
    public String viewCustomer(Model model, @RequestParam (defaultValue = "CompanyName") String sortCol,
                               @RequestParam (defaultValue = "ASC") String sortOrder, @RequestParam (defaultValue = "") String q){

        q = q.trim();
        model.addAttribute("q", q);
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortCol);
        List<MiniContractCustomerDto> concustDto = new ArrayList<>();


        if (!q.isEmpty()) {
            List<ContractCustomer> concust = repo.findAllByCompanyNameContainsOrContactNameContains(q, q, sort);
            for (ContractCustomer ccr : concust) {
                concustDto.add(contractCustomerServices.contractCustomerToMiniContractCustomerDto(ccr));
            }

        }

        else {
            List<ContractCustomer> concust = repo.findAll(sort);
            for (ContractCustomer ccr : concust) {
                concustDto.add(contractCustomerServices.contractCustomerToMiniContractCustomerDto(ccr));
            }
        }


        model.addAttribute("allContractCustomers", concustDto);
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