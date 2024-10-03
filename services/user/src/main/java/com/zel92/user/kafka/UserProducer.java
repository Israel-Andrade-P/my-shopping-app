package com.zel92.user.kafka;

import com.zel92.user.event.AccVerificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProducer {
    private final KafkaTemplate<String, AccVerificationEvent> kafkaTemplate;

    public void sendAccountVerificationMessage(AccVerificationEvent data){
        log.info("Sending message to broker");
        Message<AccVerificationEvent> message = MessageBuilder.withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "account-verification").build();
        kafkaTemplate.send(message);
        log.info("Message successfully sent");
    }
}

