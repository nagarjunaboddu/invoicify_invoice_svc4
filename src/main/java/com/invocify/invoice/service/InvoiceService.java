package com.invocify.invoice.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.invocify.invoice.exception.InvoiceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.repository.CompanyRepository;
import com.invocify.invoice.repository.InvoiceRepository;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService extends InvocifyServiceHelper {

	/**
	 * repository is getting injected using constructor provided by
	 * lombok.AllArgsConstructor
	 */
	InvoiceRepository invoiceRepository;
	CompanyRepository companyRepository;

	private static final int NUMBER_OF_ELEMENTS = 10;

	public Invoice createInvoice(InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		Company company = getCompanyOrThrowException(companyRepository, invoiceRequest.getCompany_id());
		Invoice invoice = buildInvoiceEntity(invoiceRequest, company);
		return invoiceRepository.save(invoice);
	}

	private Invoice buildInvoiceEntity(InvoiceRequest invoiceRequest, Company company) {
		return new Invoice(invoiceRequest.getAuthor(), invoiceRequest.getLineItems(), company);
	}

	public Invoice addLineItemsToInvoice(UUID invoiceId, List<LineItem> lineItems) throws InvoiceNotFoundException {
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
		invoice.getLineItems().addAll(lineItems);
		return invoiceRepository.save(invoice);
	}

  public Page<Invoice> getInvoices(int page, long chronoValue, ChronoUnit chronoUnit , boolean disableFilter) {
		Pageable sortByDateWithTenEntries = PageRequest.of(page, NUMBER_OF_ELEMENTS,
				Sort.by("createdDate").descending());
		return disableFilter? invoiceRepository.findAll(sortByDateWithTenEntries) :
				invoiceRepository.findByCreatedDateAfter(sortByDateWithTenEntries, filterDate(chronoValue, chronoUnit));
	}

	private Date filterDate(long chronoValue, ChronoUnit chronoUnit) {
		LocalDateTime filterDate = LocalDateTime.now().minus(chronoValue, chronoUnit);
		return Date.from(filterDate.atZone(ZoneId.systemDefault()).toInstant());
	}

}
