package com.invocify.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.v3.oas.annotations.Hidden;
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
	
	//TODO: add the annotation relationship once Company is declared as an entity
	@ManyToOne
	private Company company;
	private String author;
	
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	
	
	/*
	 * Initialize date before saving the entity
	 */
	@PrePersist
	private void prePersist() {
		this.createdDate = new Date();
	}

	public BigDecimal getTotalCost() {
		return BigDecimal.ZERO;
	}
}
