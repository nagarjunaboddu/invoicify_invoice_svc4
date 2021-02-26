package com.invocify.invoice.services;

import org.springframework.stereotype.Service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.entity.Invoice;
import com.invocify.invoice.repository.CompanyRepository;
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
	CompanyRepository companyRepository;

	public Invoice createInvoice(Invoice invoice) {
		//TODO : discuss the error handling in case company is not present
		Company company = companyRepository.findById(invoice.getCompany_id()).get();
		invoice.setCompany(company);
		return invoiceRepository.save(invoice);		
	}

}
