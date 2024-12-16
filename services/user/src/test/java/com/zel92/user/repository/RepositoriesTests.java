package com.zel92.user.repository;

import com.zel92.user.entity.*;
import com.zel92.user.enumeration.Authority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static com.zel92.user.enumeration.Authority.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16.4"));

    @Autowired
    UserRepository userRepository;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    ConfirmationRepository confirmationRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    public void setup(){
        UserEntity user = UserEntity.builder()
                .userId("69420")
                .email("zel@gmail")
                .firstName("Zel")
                .lastName("Drade")
                .build();
        CredentialEntity credential = new CredentialEntity(user, "123");
        ConfirmationEntity confirmation = new ConfirmationEntity(user, "6969");
        LocationEntity location = LocationEntity.builder()
                .country("Australia")
                .city("Melbourne")
                .street("69th")
                .zipCode("222")
                .user(user)
                .build();

        userRepository.save(user);
        credentialRepository.save(credential);
        confirmationRepository.save(confirmation);
        locationRepository.save(location);
    }

    @Test
    @DisplayName("Should return a user and verify results")
    public void addUser(){
        var user = userRepository.findByUserId("69420").get();

        assertThat(userRepository.findAll()).hasSize(2)
                .last()
                .extracting(UserEntity::getEmail)
                .isEqualTo("zel@gmail");
        assertThat(userRepository.findByUserId("69420")).contains(user);
        assertThat(userRepository.findByEmail("zel@gmail")).contains(user);
    }

    @Test
    @DisplayName("Should return a credential entity and verify results")
    public void credentialStuff(){
        var user = userRepository.findByUserId("69420").get();
        var credential = credentialRepository.findByUserId(user.getId()).get();

        assertThat(credentialRepository.findAll()).hasSize(1)
                .first()
                .extracting(CredentialEntity::getPassword)
                .isEqualTo("123");
        assertThat(credentialRepository.findByUserId(user.getId())).contains(credential);
    }

    @Test
    @DisplayName("Should return a confirmation and verify results")
    public void confirmationStuff(){
        var confirmation = confirmationRepository.findByKey("6969").get();

        assertThat(confirmationRepository.findAll()).hasSize(1)
                .first()
                .extracting(ConfirmationEntity::getKeyCode)
                .isEqualTo("6969");
        assertThat(confirmationRepository.findByKey("6969")).contains(confirmation);
    }

    @Test
    @DisplayName("Should return a location and verify results")
    public void locationStuff(){
        var user = userRepository.findByUserId("69420").get();
        var location = locationRepository.findByUserId(user.getId()).get();

        assertThat(locationRepository.findAll()).hasSize(1)
                .first()
                .extracting(LocationEntity::getCountry)
                .isEqualTo("Australia");
        assertThat(locationRepository.findByUserId(user.getId())).contains(location);
    }

    @Test
    @DisplayName("Check if roles are being persisted correctly")
    public void roleStuff(){
        var role = roleRepository.findByRoleName("USER").get();

        assertThat(roleRepository.findAll()).hasSize(4)
                .last()
                .extracting(RoleEntity::getName)
                .isEqualTo("SUPER_ADMIN");
        assertThat(roleRepository.findByRoleName("USER")).contains(role);
    }

}
