package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyService {

    public Company createCompany(Company company) {
        company.setId(UUID.randomUUID());
        return company;
    }
}
