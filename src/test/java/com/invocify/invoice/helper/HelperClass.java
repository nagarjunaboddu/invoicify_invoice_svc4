package com.invocify.invoice.helper;

import java.util.Date;
import java.util.UUID;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.model.InvoiceRequest;

public class HelperClass {

	public static Company requestCompany() {
		return Company.builder().name("Amazon").address("233 Siliconvalley, CA").build();
	}

	public static Company expectedCompany() {
		return Company.builder().id(UUID.randomUUID()).name("Amazon").address("233 Siliconvalley, CA").build();
	}

	public static InvoiceRequest requestInvoice(Invoice expectedInvoice) {
		return InvoiceRequest.builder().author(expectedInvoice.getAuthor()).company_id(expectedInvoice.getCompany().getId())
				.build();
	}

	public static Invoice expectedInvoice(Company expectedCompany) {
		return Invoice.builder().author("tech guy").company(expectedCompany).createdDate(new Date())
				.id(UUID.randomUUID()).build();
	}


}
