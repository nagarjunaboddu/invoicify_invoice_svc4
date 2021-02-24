package com.invocify.invoice.services;

import com.invocify.invoice.entity.LineItem;
import com.invocify.invoice.repository.LineItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LineItemServiceTest {

    LineItemRepository lineItemRepository;
    LineItemService lineItemService;

    @BeforeEach
    public void initialize() {
        lineItemRepository = mock(LineItemRepository.class);
        lineItemService = new LineItemService(lineItemRepository);
    }

    @Test
    public void createLineItem() {

        LineItem expectedLineItem = LineItem.builder()
                .description("tdd")
                .rate(new BigDecimal(10))
                .rateType("flat")
                .id(UUID.randomUUID())
                .build();

        when(lineItemRepository.save(any(LineItem.class))).thenReturn(expectedLineItem);

        LineItem actualLineItem = lineItemService.createLineItem(expectedLineItem);
        assertEquals(expectedLineItem, actualLineItem);
        verify(lineItemRepository, times(1)).save(any(LineItem.class));

    }
}
