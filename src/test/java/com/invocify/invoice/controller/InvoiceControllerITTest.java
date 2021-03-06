package com.invocify.invoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.helper.HelperClass;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	private CompanyRepository companyRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;
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
		createInvoice(1);
		mockMvc.perform(get("/api/v1/invocify/invoices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoices.length()").value(1))
				.andExpect(jsonPath("$.invoices[0].paidStatus").value(false))
				.andExpect(jsonPath("$.invoices[0].createdDate").exists())
				.andExpect(jsonPath("$.invoices[0].totalCost").value(56.5));
	}

	@Test
	public void getListOfInvoicesWithDefaultPaginationAndSort() throws Exception{
		//data setup
		for (int i =0;i<15;i++) {
			createInvoice(i);
		}

		mockMvc.perform(get("/api/v1/invocify/invoices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoices.length()").value(10))
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.totalElements").value(15))
				//validate that first element is last added element and last element is oldest added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 14"))
				.andExpect(jsonPath("$.invoices[9].lineItems[0].description").value("Service line item 5"));
	}

	@Test
	public void getListOfInvoicesWithCustomPagination() throws Exception{
		//data setup
		for (int i =0;i<15;i++) {
			createInvoice(i);
		}

		mockMvc.perform(get("/api/v1/invocify/invoices").param("page", "1"))
				.andExpect(status().isOk())
				//for second page expect only 5 values
				.andExpect(jsonPath("$.invoices.length()").value(5))
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.totalElements").value(15))
				//validate that first element is last added element and last element is oldest added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 4"))
				.andExpect(jsonPath("$.invoices[4].lineItems[0].description").value("Service line item 0"));
	}


	@Test
	public void getListOfInvoicesFilterWithDuration() throws Exception{
		//data setup
		for (int i =0;i<3;i++) {
			createInvoice(i);
			Thread.sleep(1000);
		}
		//Pass custom filter time to validate proper filtering based on filter unit
		mockMvc.perform(get("/api/v1/invocify/invoices").param("filterDuration", "2").param("filterUnit", "SECONDS"))
				.andExpect(status().isOk())
				//expect only 2 values
				.andExpect(jsonPath("$.invoices.length()").value(not(3)))
				.andExpect(jsonPath("$.invoices.length()").value(not(0)))
				//validate that first element is last added element and last element is oldest added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 2"));
	}

	@Test
	public void getListOfInvoicesFilterWithDurationAndPagination() throws Exception{
		//data setup
		for (int i =0;i<15;i++) {
			createInvoice(i);
			//Sleep for 1s to create delay in creation of invoice
			Thread.sleep(1000);
		}
		//Pass custom filter time to validate proper filtering based on filter unit
		mockMvc.perform(get("/api/v1/invocify/invoices").param("filterDuration", "12").param("filterUnit", "SECONDS"))
				.andExpect(status().isOk())
				//expect only 10 values
				.andExpect(jsonPath("$.invoices.length()").value(not(15)))
				.andExpect(jsonPath("$.invoices.length()").value(not(0)))
				.andExpect(jsonPath("$.invoices.length()").value(10))
				//validate that first element is last added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 14"));
	}

	/**
	 * Helper method to create invoices
	 */
	private void createInvoice(int count) throws Exception {
		Company company = companyRepository.save(HelperClass.requestCompany());
		Invoice invoice = HelperClass.expectedInvoice(company);
		InvoiceRequest requestInvoice = HelperClass.requestInvoice(invoice);
		LineItem lineItem = LineItem.builder().description("Service line item "+count).quantity(1).rate(new BigDecimal(15.3))
				.rateType("flat").build();
		LineItem lineItem1 = LineItem.builder().description("line item "+count).quantity(4).rate(new BigDecimal(10.3))
				.rateType("rate").build();
		requestInvoice.setLineItems(new ArrayList<LineItem>() {
			{
				add(lineItem);
				add(lineItem1);
			}
		});
		mockMvc.perform(post("/api/v1/invocify/invoices").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(requestInvoice)));
	}

	@Test
	public void getListOfAllInvoicesWithPaginationAndSorting() throws Exception{
		for (int i =0;i<10;i++) {
			createInvoice(i);
			Thread.sleep(1000);
		}
		Thread.sleep(2000);
		for (int i =10;i<20;i++) {
			createInvoice(i);
		}

		mockMvc.perform(get("/api/v1/invocify/invoices")
				.param("filterDuration", "2")
				.param("filterUnit", "SECONDS")
				.param("disableFilter","true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoices.length()").value(10))
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.totalElements").value(20))
				//validate that first element is last added element and last element is oldest added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 19"))
				.andExpect(jsonPath("$.invoices[9].lineItems[0].description").value("Service line item 10"));

		mockMvc.perform(get("/api/v1/invocify/invoices")
				.param("page","1")
				.param("filterDuration", "2")
				.param("filterUnit", "SECONDS")
				.param("disableFilter","true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.invoices.length()").value(10))
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.totalElements").value(20))
				//validate that first element is last added element and last element is oldest added element
				.andExpect(jsonPath("$.invoices[0].lineItems[0].description").value("Service line item 9"))
				.andExpect(jsonPath("$.invoices[9].lineItems[0].description").value("Service line item 0"));
  }
  
  @Test
	public void addLineItemsToNonExistingInvoice() throws Exception {
		LineItem lineItem3 = LineItem.builder().description("flat line item3").quantity(1).rate(new BigDecimal(5.5))
				.rateType("flat").build();
		LineItem lineItem4 = LineItem.builder().description("rate based line item4").quantity(3).rate(new BigDecimal(5.7))
				.rateType("rate").build();
		List<LineItem> patchLineItems = new ArrayList<>();
		patchLineItems.add(lineItem3);
		patchLineItems.add(lineItem4);
		UUID randomUUID = UUID.randomUUID();
		mockMvc.perform(patch("/api/v1/invocify/invoices/{invoiceId}/lineItems", randomUUID )
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(patchLineItems))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.[0]").value(String.format("Given Invoice not found: %s", randomUUID.toString())));
	}

	@Test
	public void addLineItemsToExistingInvoice() throws Exception {
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
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isCreated());
		Invoice invoice1 = invoiceRepository.findAll().get(0);
		LineItem lineItem3 = LineItem.builder().description("flat line item3").quantity(1).rate(new BigDecimal(5.5))
				.rateType("flat").build();
		LineItem lineItem4 = LineItem.builder().description("rate based line item4").quantity(3).rate(new BigDecimal(5.7))
				.rateType("rate").build();
		List<LineItem> patchLineItems = new ArrayList<>();
		patchLineItems.add(lineItem3);
		patchLineItems.add(lineItem4);
		mockMvc.perform(patch("/api/v1/invocify/invoices/{invoiceId}/lineItems",invoice1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(patchLineItems))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.author").value(requestInvoice.getAuthor()))
				.andExpect(jsonPath("$.createdDate").value(not(invoice1.getCreatedDate())))
				.andExpect(jsonPath("$.totalCost").value(79.1))
				.andExpect(jsonPath("$.company.id").value(company.getId().toString()))
				.andExpect(jsonPath("$.company.name").value(company.getName()))
				.andExpect(jsonPath("$.company.street").value(company.getStreet()))
				.andExpect(jsonPath("$.company.city").value(company.getCity()))
				.andExpect(jsonPath("$.company.state").value(company.getState()))
				.andExpect(jsonPath("$.company.postalCode").value(company.getPostalCode()))
				.andExpect(jsonPath("$.lineItems.length()").value(4))
				.andExpect(jsonPath("$.lineItems[2].id").exists())
				.andExpect(jsonPath("$.lineItems[2].description").value("flat line item3"))
				.andExpect(jsonPath("$.lineItems[2].quantity").value(1))
				.andExpect(jsonPath("$.lineItems[2].rateType").value("flat"))
				.andExpect(jsonPath("$.lineItems[2].rate").value(5.5))
				.andExpect(jsonPath("$.lineItems[2].totalFees").value(5.5))
				.andExpect(jsonPath("$.lineItems[3].id").exists())
				.andExpect(jsonPath("$.lineItems[3].description").value("rate based line item4"))
				.andExpect(jsonPath("$.lineItems[3].quantity").value(3))
				.andExpect(jsonPath("$.lineItems[3].rateType").value("rate"))
				.andExpect(jsonPath("$.lineItems[3].rate").value(5.7))
				.andExpect(jsonPath("$.lineItems[3].totalFees").value(17.1));
	}


	@Test
	public void updateExistingInvoice() throws Exception {
		Invoice invoice1 = getInvoiceWithTwoLineItemsAndCompany();
		Company company1 = Company.builder().name("Apple").street("430 CreditValley")
				.city("New York")
				.state("New York")
				.postalCode("75036").build();
		company1= companyRepository.save(company1);
		LineItem lineItem2 = LineItem.builder().description("Service line item 2").quantity(1).rate(new BigDecimal(150.3))
				.rateType("flat").build();

		List<LineItem> lineItemList = new ArrayList<>();
		lineItemList.add(invoice1.getLineItems().get(0));
		lineItemList.add(lineItem2);

		Invoice invoice2 = Invoice.builder().id(invoice1.getId()).author("tech person").paidStatus(true).company(company1).lineItems(lineItemList).build();

		mockMvc.perform(put("/api/v1/invocify/invoices/"+invoice1.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(invoice2)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(invoice1.getId().toString())).andExpect(jsonPath("$.author").value(invoice2.getAuthor()))
				.andExpect(jsonPath("$.createdDate").value(not(invoice1.getCreatedDate())))
				.andExpect(jsonPath("$.totalCost").value(165.6))
				.andExpect(jsonPath("$.company.id").value(company1.getId().toString()))
				.andExpect(jsonPath("$.company.name").value(company1.getName()))
				.andExpect(jsonPath("$.company.street").value(company1.getStreet()))
				.andExpect(jsonPath("$.company.city").value(company1.getCity()))
				.andExpect(jsonPath("$.company.state").value(company1.getState()))
				.andExpect(jsonPath("$.company.postalCode").value(company1.getPostalCode()))
				.andExpect(jsonPath("$.lineItems.length()").value(2))
				.andExpect(jsonPath("$.lineItems[0].id").exists())
				.andExpect(jsonPath("$.lineItems[0].description").value("Service line item"))
				.andExpect(jsonPath("$.lineItems[0].quantity").value(1))
				.andExpect(jsonPath("$.lineItems[0].rateType").value("flat"))
				.andExpect(jsonPath("$.lineItems[0].rate").value(15.3))
				.andExpect(jsonPath("$.lineItems[0].totalFees").value(15.3))
				.andExpect(jsonPath("$.lineItems[1].id").exists())
				.andExpect(jsonPath("$.lineItems[1].description").value("Service line item 2"))
				.andExpect(jsonPath("$.lineItems[1].quantity").value(1))
				.andExpect(jsonPath("$.lineItems[1].rateType").value("flat"))
				.andExpect(jsonPath("$.lineItems[1].rate").value(150.3))
				.andExpect(jsonPath("$.lineItems[1].totalFees").value(150.3));

	}

	private Invoice getInvoiceWithTwoLineItemsAndCompany() throws Exception {
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
				.content(mapper.writeValueAsString(requestInvoice))).andExpect(status().isCreated());
		Invoice invoice1 = invoiceRepository.findAll().get(0);
		return invoice1;
	}



}
