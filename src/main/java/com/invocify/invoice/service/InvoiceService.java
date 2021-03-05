package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService {

	/**
	 * repository is getting injected using constructor provided by
	 * lombok.AllArgsConstructor
	 */
	InvoiceRepository invoiceRepository;
	CompanyRepository companyRepository;

	public Invoice createInvoice(InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		Company company = companyRepository.findById(invoiceRequest.getCompany_id())
				.orElseThrow(() -> new InvalidCompanyException(invoiceRequest.getCompany_id()));
		Invoice invoice = buildInvoiceEntity(invoiceRequest, company);
		return invoiceRepository.save(invoice);
	}

	private Invoice buildInvoiceEntity(InvoiceRequest invoiceRequest, Company company) {
		return new Invoice(invoiceRequest.getAuthor(), invoiceRequest.getLineItems(), company);
	}

    public Invoice addLineItemsToExistingInvoice(UUID invoiceId, List<LineItem> lineItems) {
		Invoice invoice = invoiceRepository.findById(invoiceId).get();
		invoice.getLineItems().addAll(lineItems);
		return invoiceRepository.save(invoice);
    }
}
