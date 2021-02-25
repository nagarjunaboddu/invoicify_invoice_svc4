package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyService {

   private CompanyRepository repo;



    public Company createCompany(Company company) {
        return repo.save(company);
    }
}
