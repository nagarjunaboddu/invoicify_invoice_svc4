package com.invocify.invoice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceService {

	/**
	 * repository is getting injected using constructor provided by
	 * lombok.AllArgsConstructor
	 */
	InvoiceRepository invoiceRepository;
	CompanyRepository companyRepository;
	
	private static final int NUMBER_OF_ELEMENTS = 10;

	public Invoice createInvoice(InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		Company company = companyRepository.findById(invoiceRequest.getCompany_id())
				.orElseThrow(() -> new InvalidCompanyException(invoiceRequest.getCompany_id()));
		Invoice invoice = buildInvoiceEntity(invoiceRequest, company);
		return invoiceRepository.save(invoice);
	}

	private Invoice buildInvoiceEntity(InvoiceRequest invoiceRequest, Company company) {
		return new Invoice(invoiceRequest.getAuthor(), invoiceRequest.getLineItems(), company);
	}

	public Page<Invoice> getInvoices(int page) {
		Pageable sortByDateWithTenEntries = PageRequest.of(page, NUMBER_OF_ELEMENTS, Sort.by("createdDate").descending());
		return invoiceRepository.findAll(sortByDateWithTenEntries);
	}

}
