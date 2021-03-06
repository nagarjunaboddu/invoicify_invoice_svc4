package com.invocify.invoice.model;

import com.invocify.invoice.entity.LineItem;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceUpdateRequest extends InvoiceRequest {

    public InvoiceUpdateRequest(String author, UUID company_id, List<LineItem> lineItemList , boolean paidStatus){
        super(author,company_id,lineItemList);
        this.paidStatus= paidStatus;
    }

    private boolean paidStatus;

}
