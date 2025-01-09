package com.ecommerce.backend.controller;

import com.ecommerce.backend.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
      
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // Status code should be 200 OK
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))  // Expect JSON response
                .andExpect(content().json("{\"msg\":\"Welcome to Our Store\",\"status\":true}"));  // Check the response body
    }
}
