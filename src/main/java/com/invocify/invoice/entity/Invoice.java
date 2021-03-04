package com.invocify.invoice.entity;

import io.swagger.v3.oas.annotations.Hidden;

import javax.persistence.*;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
	private Company company;
	private String author;

	@OneToMany(cascade=CascadeType.ALL)
	private List<LineItem> lineItems;

	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	public Invoice(String author, List<LineItem> lineItems, Company company) {
		this.author = author;
		this.company = company;
		this.lineItems = lineItems;
	}
	
	private boolean paidStatus;


	/**
	 * Initializes date before saving the entity
	 */
	@PrePersist
	private void prePersist() {
		this.createdDate = new Date();
	}

	public BigDecimal getTotalCost() {
		return lineItems.stream().filter(Objects::nonNull).map(LineItem::getTotalFees).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);
	}

}
