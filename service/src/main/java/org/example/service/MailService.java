package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.dto.task.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {

    TemplateEngine engine;
    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String email;

    public void sendEmail(MailDTO mailData) throws MessagingException {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()
        );
        Context context = new Context();
        context.setVariables(mailData.getContext());
        helper.setFrom(email);
        helper.setTo(mailData.getToMail());
        helper.setSubject(mailData.getSubject());
        String html = engine.process("mail.html", context);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
