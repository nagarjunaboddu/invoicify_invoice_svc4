package com.invocify.invoice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invocify.invoice.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
