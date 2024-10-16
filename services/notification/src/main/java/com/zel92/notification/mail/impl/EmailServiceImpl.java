package com.zel92.notification.mail.impl;

import com.zel92.notification.exception.EmailErrorException;
import com.zel92.notification.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.zel92.notification.mail.EmailUtils.getEmailText;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    public static final String ACCOUNT_VERIFICATION = "Account Verification";
    public static final String ORDER_CONFIRMATION = "Order Confirmation";
    @Value("${app.info.email}")
    private String hostEmail;
    private final JavaMailSender mailSender;
    @Override
    @Async
    public void sendAccountVerificationEmail(String username, String to, String key) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(ACCOUNT_VERIFICATION);
            message.setFrom(hostEmail);
            message.setTo(to);
            message.setText(getEmailText(username, key));
            mailSender.send(message);
        }catch (Exception e){
            log.error("Unable to send email");
            throw new EmailErrorException("Email wasn't sent. Something went wrong");
        }
    }

    @Override
    @Async
    public void sendPaymentConfirmationEmail(String username, String to) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(ORDER_CONFIRMATION);
            message.setFrom(hostEmail);
            message.setTo(to);
            message.setText(getEmailText(username));
            mailSender.send(message);
        }catch (Exception e){
            log.error("Unable to send email");
            throw new EmailErrorException("Email wasn't sent. Something went wrong");
        }
    }
}
