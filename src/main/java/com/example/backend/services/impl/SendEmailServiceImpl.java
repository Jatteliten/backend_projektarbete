package com.example.backend.services.impl;
import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Optional;

import com.example.backend.services.SendEmailServices;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;


@Service
public class SendEmailServiceImpl implements SendEmailServices {

    private final JavaMailSender mailSender;

    private final ThymeleafTemplateRepo thymeleafTemplateRepo;

    public SendEmailServiceImpl(JavaMailSender mailSender, ThymeleafTemplateRepo thymeleafTemplateRepo) {
        this.mailSender = mailSender;
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
    }


    public void sendConfirmationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolvers = new StringTemplateResolver();
        templateResolvers.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolvers);

        String templateFromDatabase;
        Optional<ThymeLeafTemplates> templateOptional = thymeleafTemplateRepo.findByTitle("confirm");
        if (templateOptional.isPresent()) {
            templateFromDatabase = templateOptional.get().getHtmlTemplateString();
            Context context = new Context();
            context.setVariables(templateModel);
            String htmlBody = templateEngine.process(templateFromDatabase, context);


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } else {
            //gör felhantering
            System.out.println("mallen hittades inte");
        }



    }

}
