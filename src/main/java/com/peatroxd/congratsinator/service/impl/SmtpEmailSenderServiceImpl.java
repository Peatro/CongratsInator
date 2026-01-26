package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class SmtpEmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(List<String> to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to.toArray(String[]::new));
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
