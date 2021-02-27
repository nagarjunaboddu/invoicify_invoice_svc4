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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	/**
	 * company_id is used only one of  request attribute
	 * TODO : separate the request and entity layer
	 */
	@Transient
	@JsonIgnore
	private UUID company_id;
	
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	
	
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
