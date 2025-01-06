package com.zel92.user.controller;

import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.enumeration.Authority;
import com.zel92.user.repository.LocationRepository;
import com.zel92.user.repository.RoleRepository;
import com.zel92.user.repository.UserRepository;
import com.zel92.user.securitytest.WithMockCustomUser;
import com.zel92.user.service.JwtService;
import com.zel92.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebMvcTest(controllers = { AdminController.class })
@Import(UserServiceImpl.class)
public class AdminControllerTest {
    private static final Logger log = Logger.getLogger(AdminControllerTest.class.getName());

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    RoleRepository roleRepository;
    @MockitoBean
    LocationRepository locationRepository;
    @MockitoBean
    JwtService jwtService;

    static UserEntity admin;
    static UserEntity user;
    static RoleEntity superAdmin;

    @BeforeAll
    public static void setup(){
        log.log(Level.INFO, "[BEFORE_ALL] Setting up...");

         superAdmin = RoleEntity.builder()
                .name("SUPER_ADMIN")
                .authority(Authority.SUPER_ADMIN)
                .build();
        RoleEntity role2 = RoleEntity.builder()
                .name("USER")
                .authority(Authority.USER)
                .build();

        admin = UserEntity.builder()
                .email("someAdmin@gmail")
                .firstName("Some")
                .lastName("Admin")
                .role(superAdmin)
                .build();
        user = UserEntity.builder()
                .userId("123")
                .email("customUser@gmail")
                .firstName("Some")
                .lastName("User")
                .role(role2)
                .build();
    }

    @Test
    @DisplayName("Should fetch all users form DB")
    @WithMockCustomUser
    public void fetchAllTest(){

        List<UserEntity> users = List.of(admin);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc
                .get()
                .uri("/api/v1/admin/all")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK);

    }

    @Test
    @DisplayName("Should fail because a regular user shouldn't be able to access this endpoint")
    public void fetchAllTest2(){

        mockMvc
                .get()
                .uri("/api/v1/admin/all")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.UNAUTHORIZED);

    }

    @Test
    @DisplayName("Should switch a user's role")
    @WithMockCustomUser
    public void updateRoleTest(){

        Mockito.when(userRepository.findByUserId("123")).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleRepository.findByRoleName("SUPER_ADMIN")).thenReturn(Optional.of(superAdmin));

        mockMvc
                .get()
                .uri("/api/v1/admin/update/role")
                .param("user-id", "123")
                .param("role", "SUPER_ADMIN")
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.OK)
                .bodyText()
                .contains("Role has been updated", "/api/v1/admin/update/role");

    }

    @AfterAll
    public static void cleanup(){
        log.log(Level.INFO, "[AFTER_ALL] Cleaning up...");
        user = null;
        admin = null;
        superAdmin = null;
    }
}
