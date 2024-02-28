package com.Backend.email.implementation;

import com.Backend.email.exceptions.EmailException;
import com.Backend.email.model.NotificationEmail;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailServiceImpl {


    @Value("${mailtrap}")
    private  String FROM;

    @Value("${mailtrap.name}")
    private  String username;

    @Value("${mailtrap.pass}")
    private  String password;
    /**
     * Asynchronously sends an email based on the provided notification email.
     *
     * @param notificationEmail The notification email containing details like subject, recipient, and body.
     * @throws EmailException if there is an issue sending the email.
     */
    @Async
    public void sendEmail(NotificationEmail notificationEmail) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", FROM);
        properties.put("mail.smtp.port", "2525");
        properties.put("mail.debug", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            final String text = "text/html";
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setSubject(notificationEmail.getSubject());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificationEmail.getRecipient()));
            message.setContent(notificationEmail.getBody(), text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailException(e.getMessage());
        }
    }
}
