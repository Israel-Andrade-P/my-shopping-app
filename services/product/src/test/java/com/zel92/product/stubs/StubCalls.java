package com.zel92.product.stubs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.product.dto.response.UserResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

public class StubCalls {
    public static void stubSuperAdminCall(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserResponse user = new UserResponse(
                "123",
                "Jay Don",
                "jay@gmail",
                "SUPER_ADMIN",
                "Finland",
                "Shuaugfua",
                "69th",
                "420"
        );

        stubFor(get(urlEqualTo("/api/v1/auth/retrieve?token=" + token))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(user))));
    }

    public static void stubUserCall(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserResponse user = new UserResponse(
                "123",
                "Stan",
                "stan@gmail",
                "USER",
                "US",
                "Texas",
                "69th",
                "420"
        );

        stubFor(get(urlEqualTo("/api/v1/auth/retrieve?token=" + token))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(user))));
    }

    public static void stubInventoryCall(String productId, Integer quantity){
        stubFor(get(urlEqualTo("/api/v1/inventories/persist?product-id=" + productId + "&quantity=" + quantity))
                .willReturn(aResponse()
                        .withStatus(OK.value())));
    }
}
