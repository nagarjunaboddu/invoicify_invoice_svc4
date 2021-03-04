package com.invocify.invoice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InvoiceControllerITTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	CompanyRepository companyRepository;

	@Test
	public void createInvoicewithoutLineItem() throws Exception {

		Company company = companyRepository.save(HelperClass.requestCompany());
		Invoice invoice = HelperClass.expectedInvoice(company);
		InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);

		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$.[0]").value("Atleast one line item should be present"));

	}

	@Test
	public void createInvoicewithEmptyLineItems() throws Exception {

		Company company = companyRepository.save(HelperClass.requestCompany());
		Invoice invoice = HelperClass.expectedInvoice(company);
		InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);
		requestInvoice.setLineItems(new ArrayList<>());
		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$.[0]").value("Atleast one line item should be present"));

	}

	@Test
	public void createInvoicewithLineItems() throws Exception {

		Company company = companyRepository.save(HelperClass.requestCompany());
		Invoice invoice = HelperClass.expectedInvoice(company);
		InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);
		LineItem lineItem = LineItem.builder().description("Service line item").quantity(1).rate(new BigDecimal(15.3))
				.rateType("flat").build();
		LineItem lineItem1 = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
				.rateType("rate").build();
		requestInvoice.setLineItems(new ArrayList<LineItem>() {
			{
				add(lineItem);
				add(lineItem1);
			}
		});

		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.author").value(requestInvoice.getAuthor()))
				.andExpect(jsonPath("$.createdDate").exists()).andExpect(jsonPath("$.totalCost").value(56.5))
				.andExpect(jsonPath("$.company.id").value(company.getId().toString()))
				.andExpect(jsonPath("$.company.name").value(company.getName()))
				.andExpect(jsonPath("$.company.street").value(company.getStreet()))
				.andExpect(jsonPath("$.company.city").value(company.getCity()))
				.andExpect(jsonPath("$.company.state").value(company.getState()))
				.andExpect(jsonPath("$.company.postalCode").value(company.getPostalCode()))
				.andExpect(jsonPath("$.lineItems.length()").value(2))
				.andExpect(jsonPath("$.lineItems[0].id").exists())
				.andExpect(jsonPath("$.lineItems[0].description").value("Service line item"))
				.andExpect(jsonPath("$.lineItems[0].quantity").value(1))
				.andExpect(jsonPath("$.lineItems[0].rateType").value("flat"))
				.andExpect(jsonPath("$.lineItems[0].rate").value(15.3))
				.andExpect(jsonPath("$.lineItems[0].totalFees").value(15.3))
				.andExpect(jsonPath("$.lineItems[1].id").exists())
				.andExpect(jsonPath("$.lineItems[1].description").value("line item"))
				.andExpect(jsonPath("$.lineItems[1].quantity").value(4))
				.andExpect(jsonPath("$.lineItems[1].rateType").value("rate"))
				.andExpect(jsonPath("$.lineItems[1].rate").value(10.3))
				.andExpect(jsonPath("$.lineItems[1].totalFees").value(41.2));

	}

	@Test
	public void createInvoiceWithoutCompany() throws Exception {

		LineItem lineItem = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
				.rateType("rate").build();
		InvoiceRequest requestInvoice = InvoiceRequest.builder().author("author").lineItems(new ArrayList<LineItem>() {
			{
				add(lineItem);
			}
		}).build();
		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$.[0]").value("Invoice should be associated with an existing company"));
	}

	@Test
	public void createInvoiceWithInvalidCompany() throws Exception {
		UUID id = UUID.randomUUID();
		LineItem lineItem = LineItem.builder().description("line item").quantity(4).rate(new BigDecimal(10.3))
				.rateType("rate").build();
		InvoiceRequest requestInvoice = InvoiceRequest.builder().author("author").company_id(id)
				.lineItems(new ArrayList<LineItem>() {
					{
						add(lineItem);
					}
				}).build();
		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$.[0]").value(String.format("Given company not found: %s", id.toString())));
	}
	
	@Test
	public void getListOfInvoices() throws Exception{
		mockMvc.perform(get("/api/v1/invocify/invoices"))
		.andExpect(status().isOk());
	}

}
