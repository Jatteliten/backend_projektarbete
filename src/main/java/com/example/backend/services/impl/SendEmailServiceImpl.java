package com.example.backend.services.impl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Map;

import com.example.backend.services.SendEmailServices;


@Service
public class SendEmailServiceImpl implements SendEmailServices {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendConfirmationEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        String template = "hämta från databas";

        Context context = new Context();
        context.setVariables(templateModel);
        String htmlBody = templateEngine.process("Booking/confirmBooking.html", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subject, Map<String, Object> templateModel,String templateSource) throws MessagingException {
        Context context = new Context();
        context.setVariables(templateModel);
        String htmlBody = templateEngine.process(templateSource, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

}
