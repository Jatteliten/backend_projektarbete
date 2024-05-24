package com.example.backend.controller.AdminManageTemplates;

import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/manageTemplates")
public class AdminManageTemplatesController {
    private final ThymeleafTemplateRepo thymeleafTemplateRepo;

    public AdminManageTemplatesController(ThymeleafTemplateRepo thymeleafTemplateRepo){
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
    }

    @GetMapping("/allTemplatesFromDatabase")
    @PreAuthorize("isAuthenticated()")
    public String getTemplates(Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        System.out.println(listOfTemplates.size());
        System.out.println(listOfTemplates.get(0).getId());
        model.addAttribute("listOfTemplates",listOfTemplates);

        return "AdminTemplatesEdit/ManageTemplates.html";
    }
}
