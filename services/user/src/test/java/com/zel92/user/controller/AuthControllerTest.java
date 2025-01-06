package com.zel92.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.cache.CacheStore;
import com.zel92.user.domain.Token;
import com.zel92.user.dto.request.LocationRequest;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.*;
import com.zel92.user.enumeration.Authority;
import com.zel92.user.kafka.UserProducer;
import com.zel92.user.model.User;
import com.zel92.user.repository.*;
import com.zel92.user.security.CustomAuthenticationManager;
import com.zel92.user.security.SecurityConfig;
import com.zel92.user.service.JwtService;
import com.zel92.user.service.impl.AuthServiceImpl;
import com.zel92.user.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = { AuthController.class })
@Import({AuthServiceImpl.class, JwtServiceImpl.class, SecurityConfig.class})
public class AuthControllerTest {

    private static final Logger log = Logger.getLogger(AuthControllerTest.class.getName());

    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    JwtService jwtService;

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
    @MockitoBean
    CacheStore<String, Integer> cache;
    @MockitoBean
    CustomAuthenticationManager manager;

    static UserEntity user;
    static CredentialEntity credential;
    static ConfirmationEntity confirmation;
    static LocationEntity locationEntity;
    static UserRequest userRequest;
    static UserRequest userRequest2;
    static User model;

    @BeforeAll
    public static void setup(){
        log.log(Level.INFO, "[BEFORE_ALL] Setting up...");
        user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("zel@gmail")
                .dob(LocalDate.now())
                .telephone("304")
                .role(RoleEntity.builder().name("USER").authority(Authority.USER).build())
                .build();
        user.setId(1L);
        confirmation = ConfirmationEntity.builder().keyCode("3746862").user(user).build();
        confirmation.setCreatedAt(now());

        locationEntity = new LocationEntity("Brazil", "Fort", "69th", "320", user);
        LocationRequest location = new LocationRequest("Brazil", "Fort", "69th", "320");

        userRequest = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("zel@gmail")
                .password("12345678")
                .dob(LocalDate.now())
                .telephone("304")
                .location(location)
                .build();
        userRequest2 = UserRequest.builder()
                .email("zel@gmail")
                .password("12345678")
                .dob(LocalDate.now())
                .telephone("304")
                .location(location)
                .build();
        model = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("zel@gmail")
                .dob(LocalDate.now())
                .role("USER")
                .authorities("product:create,product:read,product:update,product:delete")
                .build();
    }
    @Test
    @DisplayName("Should create and persist user")
    public void registerUserTest() throws JsonProcessingException {

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(RoleEntity.builder().name("USER").build()));
        when(confirmationRepository.save(any(ConfirmationEntity.class))).thenReturn(confirmation);

        mockMvc
                .post()
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest))
                .exchange()
                .assertThat()
                .hasStatus(CREATED)
                .bodyText()
                .contains("Your account has been created successfully. Please check your email to enable your account.", "/api/v1/auth/register");

        verify(roleRepository).findByRoleName("USER");
    }

    @Test
    @DisplayName("Should fail")
    public void registerUserTest2() throws JsonProcessingException {

        mockMvc
                .post()
                .with(csrf())
                .uri("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequest2))
                .exchange()
                .assertThat()
                .hasStatus(BAD_REQUEST)
                .bodyText()
                .contains("Invalid fields");
    }
    @Test
    @DisplayName("Should verify and enable user")
    public void verifyAccountTest(){

        when(confirmationRepository.findByKey("3746862")).thenReturn(Optional.of(confirmation));

        when(userRepository.findByEmail("zel@gmail")).thenReturn(Optional.of(user));

        mockMvc
                .get()
                .uri("/api/v1/auth/verify")
                .param("key", "3746862")
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .contains("Your account has been verified", "/api/v1/auth/verify");

        verify(confirmationRepository).findByKey("3746862");
        verify(userRepository).findByEmail("zel@gmail");
    }

    @Test
    @DisplayName("Should validate token")
    public void validateJwtTest(){

        String jwt = jwtService.createToken(model, Token::getAccess);

        when(userRepository.findByEmail("zel@gmail")).thenReturn(Optional.of(user));

        mockMvc
                .get()
                .uri("/api/v1/auth/validate")
                .param("token", jwt)
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .contains("true");
    }

    @Test
    @DisplayName("Should fail")
    public void validateJwtTest2(){

        mockMvc
                .get()
                .uri("/api/v1/auth/validate")
                .param("token", "myToken")
                .exchange()
                .assertThat()
                .hasStatus(UNAUTHORIZED)
                .bodyText()
                .contains("Invalid compact JWT string");
    }

    @Test
    @DisplayName("Should retrieve a user")
    public void retrieveUserTest(){

        String jwt = jwtService.createToken(model, Token::getAccess);

        when(userRepository.findByEmail("zel@gmail")).thenReturn(Optional.of(user));
        when(locationRepository.findByUserId(1L)).thenReturn(Optional.of(locationEntity));

        mockMvc
                .get()
                .uri("/api/v1/auth/retrieve")
                .param("token", jwt)
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .contains("zel@gmail", "John Doe", "Brazil", "Fort", "69th", "320", "USER");
    }

    @Test
    @DisplayName("Should fail")
    public void retrieveUserTest2(){

        mockMvc
                .get()
                .uri("/api/v1/auth/retrieve")
                .param("token", "myToken")
                .exchange()
                .assertThat()
                .hasStatus(UNAUTHORIZED)
                .bodyText()
                .contains("Invalid compact JWT string");
    }



    @AfterAll
    public static void cleanup(){
        log.log(Level.INFO, "[AFTER_ALL] Cleaning up...");
        user = null;
        confirmation = null;
        userRequest = null;
        userRequest2 = null;
        locationEntity = null;
        model = null;
    }

}
