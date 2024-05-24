package com.example.backend;

import java.util.Properties;

import com.example.backend.configuration.IntegrationProperties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    final IntegrationProperties integrationProperties;

    @Autowired
    public EmailSender(IntegrationProperties integrationProperties) {
        this.integrationProperties = integrationProperties;
    }

    public void sendEmail(String recipient, String subject, String messageText) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", integrationProperties.getEthereal().isSmtpAuth());
        props.put("mail.smtp.starttls.enable", integrationProperties.getEthereal().isStarttlsEnable());
        props.put("mail.smtp.host", integrationProperties.getEthereal().getSmtpHost());
        props.put("mail.smtp.port", integrationProperties.getEthereal().getSmtpPort());

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
}

