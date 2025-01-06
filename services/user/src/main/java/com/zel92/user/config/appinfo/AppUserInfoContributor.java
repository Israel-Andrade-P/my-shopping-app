package com.zel92.user.config.appinfo;

import com.zel92.user.repository.UserRepository;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AppUserInfoContributor {
    @Bean
    public InfoContributor infoContributor(UserRepository userRepository){
        return builder -> builder.withDetail("app-user.stats", Map.of("count", userRepository.count())).build();
    }
}
