package com.invocify.invoice.controller;

import java.time.temporal.ChronoUnit;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/invocify/invoices")
@AllArgsConstructor
@Tag(name = "Invoice", description = "resources to manage invoices")
@Validated
public class InvoiceController {

	private InvoiceService invoiceService;

	private static final String DEFAULT_FILTERUNIT = "YEARS";
	private static final String DEFAULT_FILTERDURATION = "1";
	private static final String DEFAULT_PAGE = "0";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Invoice createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		return invoiceService.createInvoice(invoiceRequest);
	}

	@Operation(description = "get list of invoices created in last year, sorted by date and ten invoices per page")
	@GetMapping
	public InvoiceListResponse getInvoices(
			@PositiveOrZero @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
			@Parameter(hidden = true) @Positive @RequestParam(required = false, defaultValue = DEFAULT_FILTERDURATION) Long filterDuration,
			@Parameter(hidden = true, example = "YEARS,MONTHS,DAYS,MINUTES") @RequestParam(required = false, defaultValue = DEFAULT_FILTERUNIT) String filterUnit) {
		return invoiceResponse(invoiceService.getInvoices(page, filterDuration, ChronoUnit.valueOf(filterUnit)));
	}

	private InvoiceListResponse invoiceResponse(Page<Invoice> pages) {
		return new InvoiceListResponse(pages.toList(), pages.getTotalPages(), pages.getTotalElements());
	}
}
