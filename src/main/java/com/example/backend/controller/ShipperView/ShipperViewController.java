package com.example.backend.controller.ShipperView;

import com.example.backend.services.ShipperServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Shipper")
public class ShipperViewController {
    private final ShipperServices shipperServices;

    public ShipperViewController(ShipperServices shipperServices){
        this.shipperServices = shipperServices;
    }

    @RequestMapping("/viewAll")
    @PreAuthorize("isAuthenticated()")
    public String viewShippers(Model model){
        model.addAttribute("allShippers", shipperServices.getAllMiniShippersDto());
        return "Shipper/showShippers.html";
    }

}
