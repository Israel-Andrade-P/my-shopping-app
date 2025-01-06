package com.zel92.user.service;

import com.zel92.user.cache.CacheConfig;
import com.zel92.user.cache.CacheStore;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.enumeration.LoginType;
import com.zel92.user.kafka.UserProducer;
import com.zel92.user.repository.*;
import com.zel92.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { AuthServiceImpl.class })
@Import(CacheConfig.class)
public class AuthServiceTest {

    @Autowired
    AuthServiceImpl authService;
    @Autowired
    CacheStore<String, Integer> cache;

    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    CredentialRepository credentialRepository;
    @MockitoBean
    ConfirmationRepository confirmationRepository;
    @MockitoBean
    RoleRepository roleRepository;
    @MockitoBean
    LocationRepository locationRepository;

    @MockitoBean
    UserProducer userProducer;
    @MockitoBean
    PasswordEncoder encoder;
    @Test
    public void updateLoginAttemptTest(){
        var user = UserEntity.builder()
                .email("zel@gmail")
                .loginAttempts(0)
                .accountNonLocked(true)
                .build();

        when(userRepository.findByEmail("zel@gmail")).thenReturn(Optional.ofNullable(user));

        authService.updateLoginAttempt("zel@gmail", LoginType.LOGIN_ATTEMPT);

        assertThat(user.getLoginAttempts()).isEqualTo(1);
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    @DisplayName("Should lock user's account")
    public void updateLoginAttemptTest2(){
        var user = UserEntity.builder()
                .email("zel@gmail")
                .loginAttempts(5)
                .accountNonLocked(true)
                .build();

        when(userRepository.findByEmail("zel@gmail")).thenReturn(Optional.ofNullable(user));

        cache.put("zel@gmail", 5);

        authService.updateLoginAttempt("zel@gmail", LoginType.LOGIN_ATTEMPT);

        assertThat(user.getLoginAttempts()).isEqualTo(6);
        assertThat(user.isAccountNonLocked()).isFalse();
    }
}
