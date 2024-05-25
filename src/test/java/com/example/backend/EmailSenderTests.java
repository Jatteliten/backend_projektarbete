package com.example.backend;

import com.example.backend.configuration.IntegrationProperties;
import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmailSenderTests {
    private EmailSender emailSender;
    IntegrationProperties integrationProperties;

    private JavaMailSender javaMailSender;

    private MimeMessage mimeMessage;
    ThymeleafTemplateRepo thymeleafTemplateRepo;
    @BeforeEach
    public void setUp() {
        mimeMessage = new MimeMessage((Session)null);
        javaMailSender = mock(JavaMailSender.class);
        thymeleafTemplateRepo = mock(ThymeleafTemplateRepo.class);
        integrationProperties = mock(IntegrationProperties.class);
    }
    @Test
    public void sendEmailWithDatabaseTemplateShouldSendEmailToCorrectEmail() throws MessagingException, IOException {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSender = new EmailSender(integrationProperties,thymeleafTemplateRepo,javaMailSender);


        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("name","Martin");

        ThymeLeafTemplates thymeLeafTemplates = new ThymeLeafTemplates();
        String optionalTemplate = "<p th:text=\"${name}\"></p>";
        thymeLeafTemplates.setHtmlTemplateString(optionalTemplate);

        when(thymeleafTemplateRepo.findByTitle(anyString())).thenReturn(Optional.of(thymeLeafTemplates));

        emailSender.sendEmailWithDatabaseTemplate("martin@hej.se","hej",modelMap,"name");
        assertEquals("martin@hej.se", mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());
        assertEquals("hej", mimeMessage.getSubject());
    }
    @Test
    public void sendEmailWithDatabaseTemplateShouldSendCorrectSubject() throws MessagingException, IOException {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSender = new EmailSender(integrationProperties,thymeleafTemplateRepo,javaMailSender);


        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("name","Martin");

        ThymeLeafTemplates thymeLeafTemplates = new ThymeLeafTemplates();
        String optionalTemplate = "<p th:text=\"${name}\"></p>";
        thymeLeafTemplates.setHtmlTemplateString(optionalTemplate);

        when(thymeleafTemplateRepo.findByTitle(anyString())).thenReturn(Optional.of(thymeLeafTemplates));


        emailSender.sendEmailWithDatabaseTemplate("martin@hej.se","hej",modelMap,"name");
        assertEquals("hej", mimeMessage.getSubject());
    }
    @Test
    public void sendEmailWithDatabaseTemplateShouldThrowMessagingException() throws MessagingException, IOException {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSender = new EmailSender(integrationProperties,thymeleafTemplateRepo,javaMailSender);


        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("name","Martin");

        ThymeLeafTemplates thymeLeafTemplates = new ThymeLeafTemplates();

        when(thymeleafTemplateRepo.findByTitle(anyString())).thenReturn(Optional.of(thymeLeafTemplates));


        assertThrows(MessagingException.class, () -> {
            emailSender.sendEmailWithDatabaseTemplate("martin@hej.se", "hej", modelMap, "name");
        });
    }
    @Test
    public void sendEmailWithDatabaseTemplateShouldNotThrowMessagingException() {
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailSender = new EmailSender(integrationProperties,thymeleafTemplateRepo,javaMailSender);


        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("name","Martin");

        ThymeLeafTemplates thymeLeafTemplates = new ThymeLeafTemplates();
        String optionalTemplate = "<p th:text=\"${name}\"></p>";
        thymeLeafTemplates.setHtmlTemplateString(optionalTemplate);

        when(thymeleafTemplateRepo.findByTitle(anyString())).thenReturn(Optional.of(thymeLeafTemplates));

        try {
            emailSender.sendEmailWithDatabaseTemplate("martin@hej.se", "hej", modelMap, "name");
        } catch (MessagingException e) {
            fail("sendEmailWithDatabaseTemplate should not throw MessagingException");
        }
    }

}