package com.zel92.payment.kafka;

import com.zel92.payment.event.PaymentConfEvent;
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
public class PaymentProducer {
    private final KafkaTemplate<String, PaymentConfEvent> kafkaTemplate;

    public void sendPaymentConfMessage(PaymentConfEvent event){
        Message<PaymentConfEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "payment-confirmation")
                .build();
        kafkaTemplate.send(message);
        log.info("Message sent");
    }
}
