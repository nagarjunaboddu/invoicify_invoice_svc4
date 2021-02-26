package com.invocify.invoice.services;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.repository.InvoiceRepository;

public class InvoiceServiceTest {
	
	InvoiceService invoiceService;
	InvoiceRepository invoiceRepository;
	
	@BeforeEach
	void init(){
		invoiceRepository = mock(InvoiceRepository.class);
		invoiceService = new InvoiceService(invoiceRepository);
	}
	
	@Test
	public void createInvoice() {
		
		Invoice expectedInvoice = Invoice.builder()
				.author("tech guy")
				.company(Company.builder().id(UUID.randomUUID()).build())
				.createdDate(new Date())
				.id(UUID.randomUUID())
				.build();
		
		when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(expectedInvoice);
		
		Invoice actualInvoice = invoiceService.createInvoice(expectedInvoice);
		
		assertEquals(expectedInvoice, actualInvoice);
		assertEquals(BigDecimal.ZERO,actualInvoice.getTotalCost());
		
		verify(invoiceRepository, times(1)).save(Mockito.any(Invoice.class));
	}

}
