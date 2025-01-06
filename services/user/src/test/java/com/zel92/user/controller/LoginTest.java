package com.zel92.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.domain.UserSecurity;
import com.zel92.user.dto.request.LoginRequest;
import com.zel92.user.entity.CredentialEntity;
import com.zel92.user.model.User;
import com.zel92.user.security.CustomAuthenticationManager;
import com.zel92.user.security.CustomAuthenticationProvider;
import com.zel92.user.security.SecurityConfig;
import com.zel92.user.service.AuthService;
import com.zel92.user.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuthController.class, UserController.class, AdminController.class}))
@Import({SecurityConfig.class, CustomAuthenticationManager.class, CustomAuthenticationProvider.class, JwtServiceImpl.class})
public class LoginTest {
    private static final Logger log = Logger.getLogger(LoginTest.class.getName());

    @Autowired
    private MockMvcTester mvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    AuthService service;
    @MockitoBean
    UserDetailsService userDetailsService;

    static User user;
    static CredentialEntity credential;
    static UserSecurity userSec;
    static LoginRequest request;

    @BeforeAll
    public static void setup(){
        log.log(Level.INFO, "[BEFORE_ALL] Setting up...");
        var encoder = new BCryptPasswordEncoder();
        user = User.builder()
                .userId("123")
                .email("zel@gmail.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .build();
        credential = CredentialEntity.builder().password(encoder.encode("1234")).build();
        userSec = new UserSecurity(user, credential);
        request = new LoginRequest("zel@gmail", "1234");
    }

    @BeforeEach
    public void setup2(){
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
    }

    @Test
    @DisplayName("Should authenticate user and return a token")
    public void authTest() throws JsonProcessingException {

        when(userDetailsService.loadUserByUsername("zel@gmail")).thenReturn(userSec);

        mvc
                .post()
                .uri("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .exchange()
                .assertThat()
                .hasStatus(OK)
                .bodyText()
                .hasSizeGreaterThan(342)
                .contains("123");
    }

    @Test
    @DisplayName("Should fail cause password is incorrect")
    public void authTest2() throws JsonProcessingException {
        var request2 = new LoginRequest("zel@gmail", "12345");

        when(userDetailsService.loadUserByUsername("zel@gmail")).thenReturn(userSec);

        mvc
                .post()
                .uri("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request2))
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("BadCredentialsException" ,"Email and/or password is incorrect. Please try again");
    }

    @Test
    @DisplayName("Should fail cause user is disabled")
    public void authTest3() throws JsonProcessingException {
        user.setEnabled(false);

        when(userDetailsService.loadUserByUsername("zel@gmail")).thenReturn(userSec);

        mvc
                .post()
                .uri("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("DisabledException" ,"Your account is currently disabled");
    }

    @Test
    @DisplayName("Should fail cause account is locked")
    public void authTest4() throws JsonProcessingException {
        user.setAccountNonLocked(false);

        when(userDetailsService.loadUserByUsername("zel@gmail")).thenReturn(userSec);

        mvc
                .post()
                .uri("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("LockedException" ,"Your account is currently locked");
    }

    @Test
    @DisplayName("Should fail cause account is expired")
    public void authTest5() throws JsonProcessingException {
        user.setAccountNonExpired(false);

        when(userDetailsService.loadUserByUsername("zel@gmail")).thenReturn(userSec);

        mvc
                .post()
                .uri("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .exchange()
                .assertThat()
                .hasStatus(FORBIDDEN)
                .bodyText()
                .contains("DisabledException" ,"Your account has expired. Please contact administrator");
    }

    @AfterAll
    public static void cleanup(){
        log.log(Level.INFO, "[AFTER_ALL] Cleaning up ...");
        user = null;
        credential = null;
        userSec = null;
        request = null;
    }
}
