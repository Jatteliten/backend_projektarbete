package com.example.backend.controller.AdminManageTemplates;

import com.example.backend.EmailSender;
import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import jakarta.mail.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manageTemplates")
public class AdminManageTemplatesController {
    private final ThymeleafTemplateRepo thymeleafTemplateRepo;
    private final EmailSender emailSender;

    public AdminManageTemplatesController(ThymeleafTemplateRepo thymeleafTemplateRepo,EmailSender emailSender){
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
        this.emailSender = emailSender;
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
        model.addAttribute("templateId",templateId);
    return "AdminTemplatesEdit/customizeTemplate.html";
    }
    @RequestMapping("/editTemplate/success")
    @PreAuthorize("isAuthenticated()")
    public String editTemplateSuccess(@RequestParam String templateText,@RequestParam Long templateId, Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("message","Template updated");
        model.addAttribute("listOfTemplates",listOfTemplates);

        ThymeLeafTemplates templateFromDatabase = thymeleafTemplateRepo.findById(templateId).get();

        templateFromDatabase.setHtmlTemplateString("<div style=\"width: 100%; max-width: 80%;\"><textarea style=\"width: 100%; height: 100%; resize: none;\" readonly th:text=\"|"+templateText+"|\"></textarea></div>");
        thymeleafTemplateRepo.save(templateFromDatabase);

        return "AdminTemplatesEdit/ManageTemplates.html";
    }



    @RequestMapping("/editTemplate/testSendEmail")
    @PreAuthorize("isAuthenticated()")
    public String testSendEmail(@RequestParam Long templateId, Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("message","Test Email sent");
        model.addAttribute("listOfTemplates",listOfTemplates);

        ThymeLeafTemplates templateFromDatabase = thymeleafTemplateRepo.findById(templateId).get();
        String templateTitle = templateFromDatabase.getTitle();



        Map<String, Object> modelMap = mockModelMap();

        try {
            emailSender.sendEmailWithDatabaseTemplate("test@test.se","test send email",modelMap,templateTitle);
        } catch (MessagingException e) {
            System.out.println("Error while sending email");
        }

        return "AdminTemplatesEdit/ManageTemplates.html";
    }

    private Map<String, Object> mockModelMap() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("start", LocalDate.now());
        modelMap.put("end", LocalDate.now().plusDays(2));
        modelMap.put("customerName", "Busifer");
        modelMap.put("customerPhone", "0701234567");
        modelMap.put("pricePerNight", 100);
        modelMap.put("roomSize", 3);
        modelMap.put("amountOfNights", 7);
        modelMap.put("sundayDiscount", 20);
        modelMap.put("longStayDiscount", 12);
        modelMap.put("tenDayDiscount", 20);
        modelMap.put("fullPrice", 1000);
        modelMap.put("discountedPrice", 948);
        modelMap.put("email", "Busifer_007@mail.se");
        modelMap.put("roomId", 33);
        return modelMap;
    }
}
