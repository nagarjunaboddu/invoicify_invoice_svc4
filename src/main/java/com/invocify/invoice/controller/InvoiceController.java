package com.invocify.invoice.controller;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceListResponse;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.service.InvoiceService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/invocify/invoices")
@AllArgsConstructor
@Tag(name = "Invoice")
@Validated
public class InvoiceController {

	private InvoiceService invoiceService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Invoice createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		return invoiceService.createInvoice(invoiceRequest);
	}

	@GetMapping
	public InvoiceListResponse getInvoices(
			@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer page) {
		return invoiceResponse(invoiceService.getInvoices(page));
	}

	private InvoiceListResponse invoiceResponse(Page<Invoice> pages) {
		return new InvoiceListResponse(pages.toList(), pages.getTotalPages(), pages.getTotalElements());
	}
}
