package com.invocify.invoice.services;

import org.springframework.stereotype.Service;

import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.repository.InvoiceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceService {
	
	/** 
	 * repository is getting injected using constructor provided by  
	 * lombok.AllArgsConstructor
     */
	InvoiceRepository invoiceRepository;

	public Invoice createInvoice(Invoice invoice) {
		return invoiceRepository.save(invoice);		
	}

}
