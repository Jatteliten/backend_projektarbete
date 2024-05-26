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
import java.util.Optional;

@Controller
@RequestMapping("/manageTemplates")
@PreAuthorize("hasAuthority('Admin')")
public class AdminManageTemplatesController {
    private final ThymeleafTemplateRepo thymeleafTemplateRepo;
    private final EmailSender emailSender;

    public AdminManageTemplatesController(ThymeleafTemplateRepo thymeleafTemplateRepo,EmailSender emailSender){
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
        this.emailSender = emailSender;
    }

    @RequestMapping("/allTemplatesFromDatabase")
    @PreAuthorize("hasAuthority('Admin')")
    public String getTemplates(Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("listOfTemplates",listOfTemplates);
        return "AdminTemplatesEdit/ManageTemplates";
    }
    @RequestMapping("/editTemplate")
    @PreAuthorize("hasAuthority('Admin')")
    public String editTemplate(@RequestParam Long templateId, Model model) {
        System.out.println(templateId);
        model.addAttribute("templateId",templateId);
    return "AdminTemplatesEdit/customizeTemplate";
    }
    @RequestMapping("/editTemplate/success")
    @PreAuthorize("hasAuthority('Admin')")
    public String editTemplateSuccess(@RequestParam String templateText,@RequestParam Long templateId, Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("message","Template updated");
        model.addAttribute("listOfTemplates",listOfTemplates);

        ThymeLeafTemplates templateFromDatabase;
        Optional<ThymeLeafTemplates> optionalTemplate = thymeleafTemplateRepo.findById(templateId);


        if (optionalTemplate.isPresent()) {
            templateFromDatabase = optionalTemplate.get();
        } else {
            model.addAttribute("message","No template found");
            return "AdminTemplatesEdit/ManageTemplates";
        }

        templateFromDatabase.setHtmlTemplateString("<div style=\"width: 100%; max-width: 80%;\"><textarea style=\"width: 100%; height: 100%; resize: none;\" readonly th:text=\"|"+templateText+"|\"></textarea></div>");
        thymeleafTemplateRepo.save(templateFromDatabase);

        return "AdminTemplatesEdit/ManageTemplates";
    }
    @RequestMapping("/createNewTemplates")
    @PreAuthorize("hasAuthority('Admin')")
    public String createNewTemplates(@RequestParam String titleName, Model model) {
        if (thymeleafTemplateRepo.findByTitle(titleName).isEmpty()){
            thymeleafTemplateRepo.save(new ThymeLeafTemplates(titleName,""));
        } else {
            model.addAttribute("message","Template with title "+ titleName+" already exists, choose another name.");
        }


        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("listOfTemplates",listOfTemplates);
        return "AdminTemplatesEdit/ManageTemplates";
    }



    @RequestMapping("/editTemplate/testSendEmail")
    @PreAuthorize("hasAuthority('Admin')")
    public String testSendEmail(@RequestParam Long templateId, Model model) {
        List<ThymeLeafTemplates> listOfTemplates = thymeleafTemplateRepo.findAll();
        model.addAttribute("message","Test Email sent");
        model.addAttribute("listOfTemplates",listOfTemplates);

        String templateTitle;
        Optional<ThymeLeafTemplates> optionalTemplate = thymeleafTemplateRepo.findById(templateId);


        if (optionalTemplate.isPresent()) {
            ThymeLeafTemplates templateFromDatabase = optionalTemplate.get();
            templateTitle = templateFromDatabase.getTitle();
        } else {
            model.addAttribute("message","No template found");
            return "AdminTemplatesEdit/ManageTemplates";
        }





        Map<String, Object> modelMap = mockModelMap();

        try {
            emailSender.sendEmailWithDatabaseTemplate("test@test.se","test send email",modelMap,templateTitle);
        } catch (MessagingException e) {
            System.out.println("Error while sending email");
        }

        return "AdminTemplatesEdit/ManageTemplates";
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
