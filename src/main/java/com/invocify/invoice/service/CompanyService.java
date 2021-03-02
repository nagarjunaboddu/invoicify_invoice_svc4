package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.model.CompanyDetail;
import com.invocify.invoice.model.CompanySV;
import com.invocify.invoice.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;


    public Company createCompany(Company company) {


        return companyRepository.save(company);
    }

    public List<CompanyDetail> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return getDetailCompanies(companies);
    }

    private List<CompanyDetail> getDetailCompanies(List<Company> companies) {
        List<CompanyDetail> myNewCompanies = new ArrayList<>();
        for (Company company : companies) {
            myNewCompanies.add(new CompanyDetail(company.getName(), company.getCity(), company.getState(), company.getId(), company.getStreet(), company.getPostalCode()));
        }
        return myNewCompanies;
    }

    public List<CompanySV> getSimplifiedViewOfCompanies() {
        List<Company> companies = companyRepository.findAll();
        return getSimpleCompanies(companies);
    }

    private List<CompanySV> getSimpleCompanies(List<Company> companies) {
        List<CompanySV> myNewCompanies = new ArrayList<>();
        for (Company company : companies) {
            myNewCompanies.add(new CompanySV(company.getName(), company.getCity(), company.getState()));
        }
        return myNewCompanies;
    }
}
