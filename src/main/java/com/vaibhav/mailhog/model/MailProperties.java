package com.vaibhav.mailhog.model;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class MailProperties {
    private String toEmail;
    private String fromEmail;
    private String subject;
    private String text;
    @Nullable
    private String pathToAttachment;
}
