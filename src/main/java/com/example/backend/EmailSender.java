package com.example.backend;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.example.backend.configuration.IntegrationProperties;
import com.example.backend.model.ThymeLeafTemplates;
import com.example.backend.repos.ThymeleafTemplateRepo;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class EmailSender {
    final IntegrationProperties integrationProperties;

    private final ThymeleafTemplateRepo thymeleafTemplateRepo;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(IntegrationProperties integrationProperties, ThymeleafTemplateRepo thymeleafTemplateRepo, JavaMailSender mailSender) {
        this.integrationProperties = integrationProperties;
        this.thymeleafTemplateRepo = thymeleafTemplateRepo;
        this.mailSender = mailSender;
    }

    public void sendEmail(String recipient, String subject, String messageText) {
        Properties props = new Properties();
        //Lägg in i properties när vi vet hur det ska hanteras.
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.ethereal.email");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(integrationProperties.getEthereal().getUserName(),
                        integrationProperties.getEthereal().getPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(integrationProperties.getEthereal().getUserName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);

            System.out.println("Message sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailWithDatabaseTemplate(String to, String subject, Map<String, Object> templateModel,
                                              String templateNameInDatabase ) throws MessagingException {

        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolvers = new StringTemplateResolver();
        templateResolvers.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolvers);

        String templateFromDatabase;
        Optional<ThymeLeafTemplates> templateOptional = thymeleafTemplateRepo.findByTitle(templateNameInDatabase);

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
            //gör ordentlig felhantering
            System.out.println("mallen hittades inte, inget email skickat");
        }
    }
}

