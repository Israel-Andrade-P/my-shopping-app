package com.zel92.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.domain.Token;
import com.zel92.user.dto.request.LocationRequest;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.entity.ConfirmationEntity;
import com.zel92.user.entity.LocationEntity;
import com.zel92.user.entity.RoleEntity;
import com.zel92.user.entity.UserEntity;
import com.zel92.user.model.User;
import com.zel92.user.repository.*;
import com.zel92.user.service.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zel92.user.utils.UserUtils.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    private static final Logger log = Logger.getLogger(AuthControllerTest.class.getName());
    UserEntity userEntity;
    static UserRequest user;
    static UserRequest user2;
    static User user3;
    static LocationRequest location;
    ConfirmationEntity confirmation;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;

    @BeforeAll
    public static void beforeAll(){
        log.log(Level.INFO, "BEFORE_ALL Activated");
        location = new LocationRequest("Brazil", "Fort", "73th", "221");
        user = new UserRequest(
                "zel",
                "drade",
                "zel69@admin",
                "12345678",
                "321",
                LocalDate.of(1992, 11, 30),
                location
        );

        user2 = new UserRequest(
                "",
                "",
                "zel69@admin",
                "12345678",
                "321",
                LocalDate.of(1992, 11, 30),
                location
        );

        user3 = User.builder()
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .build();
    }
    @Test
    @DisplayName("Should create a user")
    public void registerUserTest() throws Exception {

        mockMvc.perform(
                post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Your account has been created successfully. Please check your email to enable your account.")))
                .andExpect(content().string(containsString("/api/v1/auth/register"))
                );

        Assertions.assertEquals(2, userRepository.findAll().size());
        Assertions.assertEquals(1, credentialRepository.findAll().size());
        Assertions.assertEquals(1, confirmationRepository.findAll().size());
        Assertions.assertEquals(1, locationRepository.findAll().size());
    }

    @Test
    @DisplayName("Testing for invalid requests")
    public void registerUserTest2() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user2))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid fields")));
    }

    @Test
    @DisplayName("Should verify key code and enable user")
    public void verifyAccountTest() throws Exception {
        userEntity = UserEntity.builder()
                .email("alex123@gmail.com")
                .build();
        userRepository.save(userEntity);
        String keyCode = "1234";
        confirmation = new ConfirmationEntity(userEntity, keyCode);
        confirmationRepository.save(confirmation);

        mockMvc.perform(get("/api/v1/auth/verify").param("key", keyCode))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your account has been verified")))
                .andExpect(content().string(containsString("/api/v1/auth/verify")));
    }

    @Test
    @DisplayName("Testing for bad requests")
    public void verifyAccountTest2() throws Exception {
        mockMvc.perform(get("/api/v1/auth/verify").param("key", "Blahhh"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("The key is invalid")));
    }

    @Test
    @DisplayName("Test if validates token")
    public void validateJwtTest() throws Exception {
        RoleEntity role = roleRepository.findByRoleName("USER").get();
        User user2 = fromUserEntity(userRepository.save(UserEntity.builder()
                .email("random@gmail.com")
                .role(role)
                .build()));

        String jwt = jwtService.createToken(user2, Token::getAccess);

        mockMvc.perform(get("/api/v1/auth/validate").param("token", jwt))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Send a bad token")
    public void validateJwtTest2() throws Exception {
        mockMvc.perform(get("/api/v1/auth/validate").param("token", "fakeToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid compact JWT string")));
    }

    @Test
    @DisplayName("Test if retrieves user")
    public void retrieveUserTest() throws Exception {
        RoleEntity role = roleRepository.findByRoleName("USER").get();
        UserEntity userEntity2 = UserEntity.builder().email("random2@gmail.com").role(role).build();
        User user2 = fromUserEntity(userRepository.save(userEntity2));
        locationRepository.save(new LocationEntity("Malaysia", "Guantanamo", "69th", "121", userEntity2));

        String jwt = jwtService.createToken(user2, Token::getAccess);

        mockMvc.perform(get("/api/v1/auth/retrieve").param("token", jwt))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString(userEntity2.getEmail())))
                .andExpect(MockMvcResultMatchers.content().string(containsString(userEntity2.fullName())));
    }

    @Test
    @DisplayName("Send a bad token")
    public void retrieveUserTest2() throws Exception {
        mockMvc.perform(get("/api/v1/auth/retrieve").param("token", "fakeToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid compact JWT string")));
    }
}
