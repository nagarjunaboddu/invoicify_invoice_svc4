package com.invocify.invoice.entity;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue
    @Hidden
    private UUID id;

    @ManyToOne
    @Setter
    private Company company;
    private String author;


    @Temporal(TemporalType.DATE)
    private Date createdDate;

    public Invoice(String author, Company company) {
        this.author = author;
        this.company = company;
    }


    /**
     * Initializes date before saving the entity
     */
    @PrePersist
    private void prePersist() {
        this.createdDate = new Date();
    }

    public BigDecimal getTotalCost() {
        return BigDecimal.ZERO;
    }
}
