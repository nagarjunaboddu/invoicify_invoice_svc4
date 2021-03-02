package com.invocify.invoice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

    @Id
    @GeneratedValue
    private UUID id;
    private String description;
    private BigDecimal rate;
    private String rateType;
    private Integer quantity;

    public BigDecimal getTotalFees() {
        return rateType.equals("flat") ? rate : rate.multiply(new BigDecimal(quantity));
    }

}
