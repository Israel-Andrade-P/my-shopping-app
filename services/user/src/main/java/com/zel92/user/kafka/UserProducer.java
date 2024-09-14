package com.zel92.user.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {
    private final KafkaTemplate<String, AccountVerificationDTO> kafkaTemplate;

    public void sendAccountVerificationMessage(AccountVerificationDTO dto){
        Message<AccountVerificationDTO> message = MessageBuilder.withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, "account-verification").build();
        kafkaTemplate.send(message);
    }
}

