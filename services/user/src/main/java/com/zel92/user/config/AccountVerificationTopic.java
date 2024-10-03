package com.zel92.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AccountVerificationTopic {

    @Bean
    public NewTopic accountVerification(){
        return TopicBuilder.name("account-verification").build();
    }
}
