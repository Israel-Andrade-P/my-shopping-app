package com.zel92.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.enumeration.Authority;
import com.zel92.user.repository.LocationRepository;
import com.zel92.user.repository.RoleRepository;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.securitytest.WithMockCustomUser;
import com.zel92.user.service.JwtService;
import com.zel92.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = {UserController.class})
@Import(UserServiceImpl.class)
public class UserControllerTest {

    private static final Logger log = Logger.getLogger(UserController.class.getName());

    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    RoleRepository roleRepository;
    @MockitoBean
    LocationRepository locationRepository;
    @MockitoBean
    JwtService jwtService;

    static UserEntity user;
    static UserRequest userRequest;
    @BeforeAll
    public static void setup() {
        log.log(Level.INFO, "[BEFORE_ALL] Setting up...");
        RoleEntity role = RoleEntity.builder()
                .name("USER")
                .authority(Authority.USER)
                .build();
        user = UserEntity.builder()
                .userId("123")
                .email("customUser@gmail")
                .role(role)
                .build();
        userRequest = UserRequest.builder()
                .firstName("Esteban")
                .email("customUser69@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Should fetch user by ID")
    @WithMockCustomUser
    public void fetchByIdTest() {

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(user));

        mockMvc
                .get()
                .uri("/api/v1/users/user/123")
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .contains("customUser@gmail", "123", "product:create,product:read,product:update,product:delete");
    }

    @Test
    @DisplayName("Should fail")
    @WithMockCustomUser(username = "zel@gmail", roles = {"USER"})
    public void fetchByIdTest2() {

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(user));

        mockMvc
                .get()
                .uri("/api/v1/users/user/123")
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("You don't have enough permission for that");
    }

    @Test
    @DisplayName("Should update user's info")
    @WithMockCustomUser
    public void updateUserTest() throws JsonProcessingException {

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(user));

        mockMvc
                .put()
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
                .uri("/api/v1/users/update/123")
                .exchange()
                .assertThat()
                .hasStatus(OK);

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should fail")
    @WithMockCustomUser(username = "zel@gmail", roles = {"USER"})
    public void updateUserTest2() throws JsonProcessingException {

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(user));

        mockMvc
                .put()
                .with(csrf())
                .uri("/api/v1/users/update/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("You don't have enough permission for that");
    }

    @Test
    @DisplayName("Should delete user")
    @WithMockCustomUser
    public void deleteUserTest() throws JsonProcessingException {

        when(userRepository.findByUserId("123")).thenReturn(Optional.of(user));

        mockMvc
                .delete()
                .with(csrf())
                .uri("/api/v1/users/delete/123")
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .contains("User has been deleted successfully!", "/api/v1/users/delete");

        verify(userRepository).delete(user);
    }
}
