package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompanyControllerITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void createCompanyTest_success() throws Exception {
        Company company = Company.builder().name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035").build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Amazon"))
                .andExpect(jsonPath("$.street").value("233 Siliconvalley"))
                .andExpect(jsonPath("$.city").value("LA"))
                .andExpect(jsonPath("$.state").value("California"))
                .andExpect(jsonPath("$.postalCode").value("75035"));

    }

    @Test
    public void createCompanyTest_unSuccessful_NoName() throws Exception {
        Company company = Company.builder().street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035").build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value("Name cannot be empty"));

    }

    @Test
    public void createCompanyTest_unSuccessful_NoAddress() throws Exception {
        Company company = Company.builder().name("Amazon").build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0]").value("City cannot be empty"))
                .andExpect(jsonPath("$[1]").value("PostalCode cannot be empty"))
                .andExpect(jsonPath("$[2]").value("State cannot be empty"))
                .andExpect(jsonPath("$[3]").value("Street cannot be empty"))
        ;

    }

    @Test
    public void createCompanyTest_unSuccessful_NoAddressNoName() throws Exception {
        Company company = Company.builder().build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0]").value("City cannot be empty"))
                .andExpect(jsonPath("$[1]").value("Name cannot be empty"))
                .andExpect(jsonPath("$[2]").value("PostalCode cannot be empty"))
                .andExpect(jsonPath("$[3]").value("State cannot be empty"))
                .andExpect(jsonPath("$[4]").value("Street cannot be empty"));

    }

    @Test
    public void viewCompanyDefaultSimplifiedView() throws Exception {

        Company company = Company.builder().name("Amazon")
                .street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035")
                .build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isCreated());

        Company company1 = Company.builder().name("Apple").street("430 CreditValley")
                .city("New York")
                .state("New York")
                .postalCode("75035").build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company1)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/v1/invocify/companies").param("includeDetail", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Amazon"))
                .andExpect(jsonPath("$[0].city").value("LA"))
                .andExpect(jsonPath("$[0].state").value("California"))
                .andExpect(jsonPath("$[1].name").value("Apple"))
                .andExpect(jsonPath("$[1].city").value("New York"))
                .andExpect(jsonPath("$[1].state").value("New York"));

    }

    @Test
    public void viewCompanyDetailedView() throws Exception {

        Company company = Company.builder().name("Amazon")
                .street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035")
                .build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isCreated());

        Company company1 = Company.builder().name("Apple").street("430 CreditValley")
                .city("New York")
                .state("New York")
                .postalCode("75035").build();
        mockMvc.perform(post("/api/v1/invocify/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company1)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/v1/invocify/companies").param("includeDetail", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Amazon"))
                .andExpect(jsonPath("$[0].street").value("233 Siliconvalley"))
                .andExpect(jsonPath("$[0].city").value("LA"))
                .andExpect(jsonPath("$[0].state").value("California"))
                .andExpect(jsonPath("$[0].postalCode").value("75035"))
                .andExpect(jsonPath("$[1].name").value("Apple"))
                .andExpect(jsonPath("$[1].street").value("430 CreditValley"))
                .andExpect(jsonPath("$[1].city").value("New York"))
                .andExpect(jsonPath("$[1].state").value("New York"))
                .andExpect(jsonPath("$[1].postalCode").value("75035"));


    }


}
