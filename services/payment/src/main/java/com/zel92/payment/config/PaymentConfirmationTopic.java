package com.zel92.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class PaymentConfirmationTopic {

    @Bean
    public NewTopic paymentConfirmation(){
        return TopicBuilder.name("payment-confirmation").build();
    }
}
