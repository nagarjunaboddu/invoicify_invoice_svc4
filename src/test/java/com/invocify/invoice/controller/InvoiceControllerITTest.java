package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.repository.CompanyRepository;
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
public class InvoiceControllerITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void createInvoice() throws Exception {

        Company expectedCompany = companyRepository.save(HelperClass.requestCompany());
        Invoice invoice = HelperClass.expectedInvoice(expectedCompany);
        Invoice invoice1 = HelperClass.requestInvoice(invoice);

        mockMvc.perform(post("/api/v1/invocify/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoice1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").value(invoice.getAuthor()))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.totalCost").value(0))
                .andExpect(jsonPath("$.company.id").value(expectedCompany.getId().toString()))
                .andExpect(jsonPath("$.company.name").value(expectedCompany.getName()))
                .andExpect(jsonPath("$.company.address").value(expectedCompany.getAddress()));

    }
}
