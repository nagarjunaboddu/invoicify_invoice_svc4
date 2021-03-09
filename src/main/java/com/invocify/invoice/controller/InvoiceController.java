package com.invocify.invoice.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.invocify.invoice.exception.InvoiceAlreadyPaidException;
import com.invocify.invoice.exception.InvoiceNotFoundException;
import com.invocify.invoice.model.InvoiceUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.BaseResponse;
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
	public BaseResponse createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) throws InvalidCompanyException {
		return new BaseResponse(invoiceService.createInvoice(invoiceRequest));
	}

	@Operation(description = "get list of invoices created in last year, sorted by date and ten invoices per page")
	@GetMapping
	public BaseResponse getInvoices(
			@PositiveOrZero @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
			@RequestParam(required = false) boolean disableFilter,
			@Parameter(hidden = false) @Positive @RequestParam(required = false, defaultValue = DEFAULT_FILTERDURATION) Long filterDuration,
			@Parameter(hidden = false, example = "YEARS,MONTHS,DAYS,MINUTES") @RequestParam(required = false, defaultValue = DEFAULT_FILTERUNIT) String filterUnit) {
		return invoiceResponse(invoiceService.getInvoices(page, filterDuration, ChronoUnit.valueOf(filterUnit),disableFilter));
	}

	@PutMapping("/{invoiceId}")
	public  BaseResponse updateInvoice(@PathVariable UUID invoiceId , @Valid @RequestBody InvoiceUpdateRequest invoiceUpdateRequest) throws InvoiceNotFoundException, InvoiceAlreadyPaidException, InvalidCompanyException {
		return  new BaseResponse(invoiceService.updateInvoice(invoiceId,invoiceUpdateRequest));
	}

	private BaseResponse invoiceResponse(Page<Invoice> pages) {
		return  new BaseResponse(new InvoiceListResponse(pages.toList(), pages.getTotalPages(), pages.getTotalElements()));
	}


    @PatchMapping("/{invoiceId}/lineItems")
    @ResponseStatus(HttpStatus.OK)
    public  BaseResponse addLineItemsToExistingInvoice(@PathVariable UUID invoiceId , @RequestBody List<LineItem> lineItems) throws InvoiceNotFoundException, InvoiceAlreadyPaidException {
        return  new BaseResponse(invoiceService.addLineItemsToInvoice(invoiceId, lineItems));

    }
}
