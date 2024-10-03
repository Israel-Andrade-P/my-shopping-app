package com.zel92.notification.consumer;

import com.zel92.notification.mail.EmailService;
import com.zel92.user.event.AccVerificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "account-verification")
    public void accountVerificationConsumer(AccVerificationEvent data) {
        log.info(String.format("Consuming message from account-verification Topic:: %s", data));
        emailService.sendAccountVerificationEmail(data.getFullName().toString(), data.getEmail().toString(), data.getKey().toString());
        log.info("Message consumed");
    }
}
