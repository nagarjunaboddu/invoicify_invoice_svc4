package com.invocify.invoice.controller;

import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.InvoiceRequest;
import com.invocify.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

    @PatchMapping("/{invoiceId}/lineItems")
    @ResponseStatus(HttpStatus.OK)
    public Invoice addLineItemsToExistingInvoice(@PathVariable UUID invoiceId , @RequestBody List<LineItem> lineItems) {
        return invoiceService.addLineItemsToExistingInvoice(invoiceId, lineItems);
    }
}
