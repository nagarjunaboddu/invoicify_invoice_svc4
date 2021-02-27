package com.invocify.invoice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class InvoiceRequest {

    private String author;
    private UUID company_id;
}
