package com.invocify.invoice.controller;

import com.invocify.invoice.entity.Invoice;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invocify/invoices")
@AllArgsConstructor
@Tag(name = "Invoice")
public class InvoiceController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInvoice(@RequestBody Invoice invoice) {

    }
}
