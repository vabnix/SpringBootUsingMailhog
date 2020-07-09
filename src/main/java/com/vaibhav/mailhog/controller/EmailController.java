package com.vaibhav.mailhog.controller;

import com.vaibhav.mailhog.config.EmailConfig;
import com.vaibhav.mailhog.model.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@RestController
public class EmailController {

    private static final String SEND_EMAIL = "/send/email";
    private static final String SEND_EMAIL_WITH_ATTACHMENT = "/send/attachment";

    private final EmailConfig emailConfig;

    @Autowired
    public EmailController(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @PostMapping(value = SEND_EMAIL)
    public void sendEmail(@RequestBody MailProperties mailProperties) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getFromEmail());
        message.setTo(mailProperties.getToEmail());
        message.setSubject(mailProperties.getSubject());
        message.setText(mailProperties.getText());
        emailConfig.Config().send(message);
    }

    @PostMapping(value = SEND_EMAIL_WITH_ATTACHMENT)
    public void sendEmailWithAttachment(@RequestBody MailProperties mailProperties) throws MessagingException {
        MimeMessage message = emailConfig.Config().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getFromEmail());
            helper.setTo(mailProperties.getToEmail());
            helper.setSubject(mailProperties.getSubject());
            helper.setText(mailProperties.getText());

            if (mailProperties.getPathToAttachment() != null) {
                MimeBodyPart emailAttachment = new MimeBodyPart();
                emailAttachment.attachFile("S3/Page.pdf");

                FileSystemResource resource = new FileSystemResource(new File("S3/Page.pdf"));
                helper.addAttachment(resource.getFilename(), resource);
                emailConfig.Config().send(message);
            }
        } catch (MailException | IOException ex) {
            System.err.println(ex);
        }

        emailConfig.Config().send(message);
    }


}
