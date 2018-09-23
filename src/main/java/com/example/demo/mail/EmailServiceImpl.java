package com.example.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
@EnableAsync
public class EmailServiceImpl {

    @Autowired
    public JavaMailSender emailSender;

    @Async
    public void sendSimpleMessage(
        String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage ();
        message.setTo (to);
        message.setSubject (subject);
        message.setText (text);
        emailSender.send (message);
    }

    @Async
    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment) {

        MimeMessage message = emailSender.createMimeMessage ();

        MimeMessageHelper helper = null;
        try {
            helper.setTo (to);
            helper.setSubject (subject);
            helper.setText (text);
            helper = new MimeMessageHelper (message, true);
            FileSystemResource file
                    = new FileSystemResource (new File (pathToAttachment));
            helper.addAttachment (file.getFilename (), file);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace ();
        }
        emailSender.send (message);
    }
}