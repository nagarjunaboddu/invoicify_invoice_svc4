package com.invocify.invoice.helper;


import java.util.ArrayList;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.model.InvoiceRequest;

import java.util.Date;
import java.util.UUID;

public class HelperClass {


	public static Invoice expectedInvoice(Company expectedCompany) {
		return Invoice.builder().author("tech guy")
				.company(expectedCompany)
				.createdDate(new Date())
				.id(UUID.randomUUID())
				.lineItems(new ArrayList<>())
				.build();
	}

    public static Company requestCompany() {
        return Company.builder().name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035").build();
    }

    public static Company expectedCompany() {
        return Company.builder().id(UUID.randomUUID()).name("Amazon").street("233 Siliconvalley")
                .city("LA")
                .state("California")
                .postalCode("75035")
                .build();
    }

    public static InvoiceRequest requestInvoice(Invoice expectedInvoice) {
        return InvoiceRequest.builder().author(expectedInvoice.getAuthor()).company_id(expectedInvoice.getCompany().getId())
                .build();
    }



}
