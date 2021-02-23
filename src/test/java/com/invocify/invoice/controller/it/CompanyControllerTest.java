package com.invocify.invoice.controller.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

        @Autowired
        MockMvc mockMvc;

        @Test
        public void createCompanyTest_success() throws Exception {

            mockMvc.perform(post("/api/v1/company")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("company name"))
                    .andExpect(status().isCreated());

        }



}
