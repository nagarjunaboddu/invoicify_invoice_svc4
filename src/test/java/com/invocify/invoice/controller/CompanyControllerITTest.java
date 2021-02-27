package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerITTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper mapper;

        @Test
        public void createCompanyTest_success() throws Exception {
                Company company =  Company.builder().name("Amazon").address("233 Siliconvalley, CA").build();
            mockMvc.perform(post("/api/v1/invocify/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(company)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Amazon"))
                    .andExpect(jsonPath("$.address").value("233 Siliconvalley, CA"));

        }

        @Test
        public void createCompanyTest_unSuccessful_NoName() throws Exception {
                Company company =  Company.builder().address("233 Siliconvalley, CA").build();
                mockMvc.perform(post("/api/v1/invocify/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.length()").value(1))
                        .andExpect(jsonPath("$[0]").value("Name cannot be empty"))
                ;

        }
        @Test
        public void createCompanyTest_unSuccessful_NoAddress() throws Exception {
                Company company =  Company.builder().name("Amazon").build();
                mockMvc.perform(post("/api/v1/invocify/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.length()").value(1))
                        .andExpect(jsonPath("$[0]").value("Address cannot be empty"))
                ;

        }
        @Test
        public void createCompanyTest_unSuccessful_NoAddressNoName() throws Exception {
                Company company =  Company.builder().build();
                mockMvc.perform(post("/api/v1/invocify/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0]").value("Address cannot be empty"))
                        .andExpect(jsonPath("$[1]").value("Name cannot be empty"))
                ;

        }



}
