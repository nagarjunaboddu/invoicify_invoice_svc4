package com.invocify.invoice.services;

import com.invocify.invoice.entity.LineItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineItemServiceTest {

    @Test
    public void createLineItem() {

        LineItemService lineItemService = new LineItemService();
        LineItem lineItem = lineItemService.createLineItem();
        assertEquals(null, lineItem);

    }
}
