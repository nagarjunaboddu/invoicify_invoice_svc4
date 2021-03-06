package com.invocify.invoice.service;

import com.invocify.invoice.entity.Company;
import com.invocify.invoice.exception.InvalidCompanyException;
import com.invocify.invoice.model.CompanyDetail;
import com.invocify.invoice.model.CompanySV;
import com.invocify.invoice.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService extends AbstractInvocifyService {

    private CompanyRepository companyRepository;


    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }


    public List<? extends CompanySV> getAllCompanies(boolean includeDetail, boolean includeInactive) {
        List<Company> companies;
        if (includeInactive) {
            companies = companyRepository.findAll();
        } else {
            companies = companyRepository.findByActiveTrue();
        }


        if (includeDetail) {
            return companies.stream().map(company -> {
                CompanyDetail companyDetail = new CompanyDetail();
                BeanUtils.copyProperties(company, companyDetail);
                return companyDetail;
            }).collect(Collectors.toList());

        } else {
            return companies.stream().map(company -> {
                CompanySV companySV = new CompanySV();
                BeanUtils.copyProperties(company, companySV);
                return companySV;
            }).collect(Collectors.toList());
        }
    }

    public Company archiveCompany(UUID companyId) throws InvalidCompanyException {
        Company company = getCompanyOrThrowException(companyRepository, companyId);
        company.setActive(false);
        return companyRepository.save(company);
    }
    
    public Company modifyCompany(UUID companyId, Company companyRequest) throws InvalidCompanyException {    
    	getCompanyOrThrowException(companyRepository, companyId);
    	companyRequest.setId(companyId);
    	return companyRepository.save(companyRequest);
    }
}
