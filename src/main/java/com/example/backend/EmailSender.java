package com.example.backend;

import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {
    public static void main(String[] args) {
        // Konfigurationsinställningar för e-postservern
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.ethereal.email");
        props.put("mail.smtp.port", "587");

        // Skapa en session med autentisering
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("delphia.oberbrunner12@ethereal.email", "QGBmHrKQTnf6FyDgmR");
            }
        });

        try {
            // Skapa ett MimeMessage-objekt
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("delphia.oberbrunner12@ethereal.email"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mottagare@example.com"));
            message.setSubject("Test Email");
            message.setText("Hej! Detta är ett testmeddelande från JavaMail.");

            // Skicka meddelandet
            Transport.send(message);

            System.out.println("Meddelandet har skickats framgångsrikt!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
