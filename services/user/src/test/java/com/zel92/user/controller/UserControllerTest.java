package com.zel92.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.entity.CredentialEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.repository.CredentialRepository;
import com.zel92.user.repository.RoleRepository;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.securitytest.WithMockCustomUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder encoder;

    @Test
    @DisplayName("Should get a user from DB by userId")
    @WithMockCustomUser
    public void fetchById() throws Exception {
        var role = roleRepository.findByRoleName("USER").get();
        var userEntity = userRepository.save(UserEntity.builder().userId("1234").email("customUser").role(role).build());
        credentialRepository.save(new CredentialEntity(userEntity, encoder.encode("123")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/user/" + userEntity.getUserId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("customUser")))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("1234")));
    }
}
