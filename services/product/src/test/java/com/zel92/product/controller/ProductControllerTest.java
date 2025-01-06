package com.zel92.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.product.dto.request.ProductRequest;
import com.zel92.product.entity.CategoryEntity;
import com.zel92.product.entity.ProductEntity;
import com.zel92.product.exception.AuthorizationFailedException;
import com.zel92.product.exception.ProductNotFoundException;
import com.zel92.product.repository.CategoryRepository;
import com.zel92.product.repository.ProductRepository;
import com.zel92.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.zel92.product.enumeration.CategoryType.ELECTRONICS;
import static com.zel92.product.stubs.StubCalls.*;
import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@Import(ProductServiceImpl.class)
@AutoConfigureWireMock(port = 0)
class ProductControllerTest {
    private static final Logger log = Logger.getLogger(ProductControllerTest.class.getName());

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ProductRepository productRepository;
    @MockBean
    CategoryRepository categoryRepository;

    static ProductRequest productRequest;
    static String fakeToken;
    static ProductEntity product;
    static CategoryEntity category;

    @BeforeAll
    public static void setup() {
        log.log(Level.INFO, "[BEFORE_ALL] Setting up..");

        fakeToken = "fake-token";
        productRequest = new ProductRequest(
                "Playstation 5",
                "Pure awesomeness",
                BigDecimal.valueOf(599.99),
                3,
                "Electronics");
        product = ProductEntity.builder()
                .name("Playstation 5")
                .productId("999")
                .build();
        category = CategoryEntity.builder()
                .type(ELECTRONICS)
                .build();
    }

    @Test
    @DisplayName("Should return a 201")
    void addProductTest() throws Exception {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);
        when(categoryRepository.findByType("Electronics")).thenReturn(ofNullable(category));

        stubSuperAdminCall(fakeToken);
        stubInventoryCall(product.getProductId(), productRequest.quantity());

        mvc.perform(
                post("/api/v1/products/add")
                        .header(AUTHORIZATION, "Bearer " + fakeToken)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Product has been added successfully")))
                .andExpect(content().string(containsString("/api/v1/products/add")));
    }

    @Test
    @DisplayName("Should throw an Authorization exception")
    void addProductTest2() throws Exception {

        stubUserCall(fakeToken);

        mvc.perform(
                        post("/api/v1/products/add")
                                .header(AUTHORIZATION, "Bearer " + fakeToken)
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(productRequest))
                )
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AuthorizationFailedException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("You don't have enough permission for that", result.getResolvedException().getMessage()));

    }

    @Test
    @DisplayName("Should return a product based on the ID provided")
    public void getProductByIdTest() throws Exception {

        when(productRepository.findByProductId(product.getProductId())).thenReturn(ofNullable(product));

        mvc.perform(get("/api/v1/products/search/" + product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(product.getName())))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(product.getProductId())));
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException")
    public void getProductByIdTest2() throws Exception {
        String invalidId = "Invalid ID";

       when(productRepository.findByProductId(invalidId)).thenThrow(new ProductNotFoundException("Product with ID: " + invalidId + " not found"));

        mvc.perform(get("/api/v1/products/search/" + invalidId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product with ID: " + invalidId + " not found")));
    }

}
