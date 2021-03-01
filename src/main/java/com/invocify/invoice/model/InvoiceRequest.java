package com.invocify.invoice.model;

import com.invocify.invoice.entity.LineItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InvoiceRequest {

    private String author;
    private UUID company_id;
    @NotEmpty(message="Atleast one line item should be present")
    private List<LineItem> lineItems;
}
