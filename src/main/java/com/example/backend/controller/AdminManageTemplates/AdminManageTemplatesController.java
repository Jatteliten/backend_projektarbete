package com.example.backend.controller.AdminManageTemplates;

import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/manageTemplates")
public class AdminManageTemplatesController {
    private final ThymeleafTemplateRepo thymeleafTemplateRepo;

    public AdminManageTemplatesController(ThymeleafTemplateRepo thymeleafTemplateRepo){
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
    }

    @RequestMapping("/allTemplatesFromDatabase")
    @PreAuthorize("isAuthenticated()")
    public String getTemplates(Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("listOfTemplates",listOfTemplates);
        return "AdminTemplatesEdit/ManageTemplates.html";
    }
    @RequestMapping("/editTemplate")
    @PreAuthorize("isAuthenticated()")
    public String editTemplate(@RequestParam Long templateId, Model model) {
        System.out.println(templateId);
    return "AdminTemplatesEdit/customizeTemplate.html";
    }
}
