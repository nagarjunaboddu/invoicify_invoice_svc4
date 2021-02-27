package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.helper.HelperClass;
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
public class InvoiceControllerITTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void createInvoice() throws Exception {

        Company expectedCompany = HelperClass.expectedCompany();
        Invoice invoice = HelperClass.expectedInvoice(expectedCompany);
        
        mockMvc.perform(post("/api/v1/invocify/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(HelperClass.requestInvoice(invoice))))
                .andExpect(status().isCreated());

    }
}
