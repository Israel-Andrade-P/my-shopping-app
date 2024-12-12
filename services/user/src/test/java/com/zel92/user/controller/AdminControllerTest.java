package com.zel92.user.controller;

import com.zel92.user.securitytest.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("Should return all users from DB")
    @WithMockCustomUser
    public void fetchAllTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Should block request")
    public void fetchAllTest2() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/all"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
