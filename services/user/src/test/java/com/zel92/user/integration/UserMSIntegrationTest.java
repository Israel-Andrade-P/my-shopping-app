package com.zel92.user.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.dto.request.LocationRequest;
import com.zel92.user.dto.request.UserRequest;
import com.zel92.user.event.AccVerificationEvent;
import com.zel92.user.kafka.UserProducer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9094"}, partitions = 1)
public class UserMSIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16.4"));
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
            .asCompatibleSubstituteFor("apache/kafka"));

    @DynamicPropertySource
    static void initKafkaProps(DynamicPropertyRegistry registry){
        //Bootstrap server will be different everytime, so get it from the kafka container
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.properties.schema.registry.url", () -> kafkaContainer.getBootstrapServers().replace("9092", "8081"));
    }

    @Autowired
    MockMvcTester mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserProducer producer;

    static UserRequest userRequest;
    static LocationRequest location;

    @BeforeAll
    public static void setup(){

        location = new LocationRequest("Brazil", "Fort", "69th", "320");

        userRequest = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("zel@gmail")
                .password("12345678")
                .dob(LocalDate.now())
                .telephone("304")
                .location(location)
                .build();
    }
    @Test
    public void kafkaTest() throws JsonProcessingException {

        producer.sendAccountVerificationMessage(new AccVerificationEvent("Zel Andra" ,"zel@gmail.com", "1234"));
//        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
//
//        });

//        mvc
//                .post()
//                .uri("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(userRequest))
//                .exchange()
//                .assertThat()
//                .hasStatus(HttpStatus.CREATED);
    }
}
