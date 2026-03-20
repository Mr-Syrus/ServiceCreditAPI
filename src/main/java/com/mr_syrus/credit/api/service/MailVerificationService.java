package com.mr_syrus.credit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class MailVerificationService {

    private final JavaMailSender mailSender;

    public MailVerificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateVericationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("мое мыло"); //ВСТАВИТЬ РЕЛЕВАНТНЫЕ ДАННЫЕ
        message.setTo(toEmail);
        message.setSubject("Код подтверждения");
        message.setText("Ваш код подтверждения: " + code + "\nКод дествителен 10 минут");

        mailSender.send(message);
    }
}
