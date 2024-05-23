package com.example.backend;

import java.util.Properties;

import com.example.backend.configuration.EtherealProperties;
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


    public void sendEmail(String recipient, String subject, String messageText) {

        //EtherealProperties ethereal = properties.getEthereal();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.ethereal.email");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("delphia.oberbrunner12@ethereal.email", "QGBmHrKQTnf6FyDgmR");
                //return new PasswordAuthentication(ethereal.getUserName(), ethereal.getPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            //message.setFrom(new InternetAddress(ethereal.getUserName()));
            message.setFrom(new InternetAddress("delphia.oberbrunner12@ethereal.email"));
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

